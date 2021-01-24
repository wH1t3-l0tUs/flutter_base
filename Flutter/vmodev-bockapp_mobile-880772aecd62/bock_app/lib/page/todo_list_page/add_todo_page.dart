import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterbloc/config/global.dart';
import 'package:flutterbloc/model/todo_item.dart';
import 'package:flutterbloc/utils/increase_number_database.dart';

import 'todo_bloc/bloc.dart';

class AddTodoPage extends StatefulWidget {
  AddTodoPage({Key key}) : super(key: key);

  @override
  _AddTodoPageState createState() {
    return _AddTodoPageState();
  }
}

class _AddTodoPageState extends State<AddTodoPage> {
  TextEditingController noteController = TextEditingController();
  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    noteController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final TodoBloc todoBloc = BlocProvider.of<TodoBloc>(context);
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          title: Text(
            "Add Todo",
            style: AppTextStyle.whiteExtraText,
          ),
          leading: IconButton(
            onPressed: () {
              Navigator.pop(context);
            },
            icon: Icon(Icons.arrow_back),
          ),
          centerTitle: true,
        ),
        body: Container(
          width: MediaQuery.of(context).size.width,
          height: MediaQuery.of(context).size.height,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              TextField(
                controller: noteController,
                decoration: const InputDecoration(
                    border: OutlineInputBorder(
                        borderRadius: BorderRadius.all(Radius.circular(16.0))),
                    hintText: "Add note",
                    labelText: "Note"),
              ),
              const SizedBox(
                height: 30.0,
              ),
              RaisedButton(
                onPressed: () async {
                  var listTodo = await TodoItem().getModel() as List<TodoItem>;
                  TodoItem item = TodoItem(
                      note: noteController.text,
                      isFinish: false,
                      index: GenerateAutoIncrease()
                          .generateNumberAutoIncrease(listTodo));
                  todoBloc.add(AddTodo(item));
                  Navigator.pop(context);
                },
                shape: const RoundedRectangleBorder(
                    borderRadius: BorderRadius.all(Radius.circular(32.0))),
                child: const Text("Add Todo"),
              )
            ],
          ),
        ),
      ),
    );
  }
}
