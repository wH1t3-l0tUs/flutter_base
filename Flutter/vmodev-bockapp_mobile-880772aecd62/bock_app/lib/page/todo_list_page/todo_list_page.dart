import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterbloc/config/global.dart';
import 'package:flutterbloc/model/todo_item.dart';
import 'package:flutterbloc/page/login_page/login_bloc/bloc.dart';
import 'package:flutterbloc/page/login_page/login_page.dart';
import 'package:flutterbloc/page/todo_list_page/add_todo_page.dart';

import 'todo_bloc/bloc.dart';

class TodoListPage extends StatefulWidget {
  TodoListPage({Key key}) : super(key: key);

  @override
  _TodoListPageState createState() {
    return _TodoListPageState();
  }
}

class _TodoListPageState extends State<TodoListPage> {
  int currentTab = 0;
  List<TodoItem> listItem = [];

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final TodoBloc todoBloc = BlocProvider.of<TodoBloc>(context);
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          title: Text(
            "Todo List",
            style: AppTextStyle.whiteExtraText,
          ),
          centerTitle: true,
          backgroundColor: Colors.blue,
          actions: <Widget>[
            IconButton(
              onPressed: () {
                Global.storage.remove("isLogin");
                Navigator.pushAndRemoveUntil(
                    context,
                    CupertinoPageRoute(
                        builder: (context) => BlocProvider(
                            create: (BuildContext context) => LoginBloc(),
                            child: LoginPage())),
                    (route) => false);
              },
              icon: Icon(Icons.exit_to_app),
            )
          ],
        ),
        body: Container(
          width: MediaQuery.of(context).size.width,
          height: MediaQuery.of(context).size.height,
          child: Column(
            children: <Widget>[
              Container(
                width: MediaQuery.of(context).size.width,
                child: CupertinoSegmentedControl<int>(
                  children: {
                    0: const Padding(
                      padding: EdgeInsets.all(10.0),
                      child: Text("TODO"),
                    ),
                    1: const Padding(
                      padding: EdgeInsets.all(10.0),
                      child: Text("DONE"),
                    )
                  },
                  onValueChanged: (int newValue) {
                    currentTab = newValue;
                    if (newValue == 0) {
                      todoBloc.add(GetTodoList());
                    } else {
                      todoBloc.add(GetDoneList());
                    }
                    setState(() {});
                  },
                  groupValue: currentTab,
                ),
              ),
              const SizedBox(
                height: 20.0,
              ),
              Expanded(
                child: BlocBuilder<TodoBloc, TodoState>(
                  builder: (context, state) {
                    if (state is TodoLoading) {
                      return const Center(
                        child: CircularProgressIndicator(),
                      );
                    } else if (state is TodoLoaded) {
                      if (state.listTodoItem.isEmpty) {
                        return Center(
                          child: Text(
                            "To do is empty \n Add more to do now",
                            textAlign: TextAlign.center,
                            style: AppTextStyle.blackNormalText,
                          ),
                        );
                      } else {
                        return ListView.separated(
                          separatorBuilder: (context, index) => const Divider(),
                          itemBuilder: (context, index) => buildItemTodo(
                              state.listTodoItem[index], todoBloc),
                          itemCount: state.listTodoItem.length,
                        );
                      }
                    } else {
                      return Container();
                    }
                  },
                ),
              )
            ],
          ),
        ),
        floatingActionButton: FloatingActionButton(
          child: Icon(Icons.add),
          onPressed: () {
            Navigator.push(
                context,
                CupertinoPageRoute(
                    builder: (context) => BlocProvider.value(
                        value: todoBloc, child: AddTodoPage())));
          },
          mini: true,
        ),
      ),
    );
  }

  Widget buildItemTodo(TodoItem item, TodoBloc todoBloc) {
    return CheckboxListTile(
      title: Text(
        item.note,
        style: AppTextStyle.blackLargeText,
      ),
      value: item.isFinish,
      controlAffinity: ListTileControlAffinity.leading,
      onChanged: item.isFinish
          ? null
          : (isCheck) {
              if (isCheck) {
                todoBloc.add(UpdateTodo(item));
                setState(() {});
              }
            },
    );
  }
}
