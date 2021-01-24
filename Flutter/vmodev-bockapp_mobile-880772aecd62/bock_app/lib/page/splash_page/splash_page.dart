import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterbloc/config/global.dart';
import 'package:flutterbloc/database/app_database.dart';
import 'package:flutterbloc/page/login_page/login_bloc/bloc.dart';
import 'package:flutterbloc/page/login_page/login_page.dart';
import 'package:flutterbloc/page/todo_list_page/todo_bloc/bloc.dart';
import 'package:flutterbloc/page/todo_list_page/todo_list_page.dart';
import 'package:flutterbloc/repository/todo_repository.dart';
import 'package:flutterbloc/utils/multi_languages_key.dart';
import 'package:easy_localization/easy_localization.dart';

class SplashPage extends StatefulWidget {
  SplashPage({Key key}) : super(key: key);

  @override
  _SplashPageState createState() {
    return _SplashPageState();
  }
}

class _SplashPageState extends State<SplashPage> {
  @override
  void initState() {
    super.initState();
    initDatabase();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: Center(
            child: Text(
          LanguagesKey.title,
          style: AppTextStyle.blackNormalText,
        ).tr()),
      ),
    );
  }

  void initDatabase() {
    AppDatabase.initDatabase().then((value) async {
      bool isLogin = Global.storage.getBool("isLogin") ?? false;
      await Future.delayed(const Duration(seconds: 2)).then((value) {
        if (isLogin) {
          var todoRepository = TodoRepository();
          Navigator.pushAndRemoveUntil(
              context,
              CupertinoPageRoute(
                  builder: (context) => BlocProvider(
                      create: (context) =>
                          TodoBloc(todoRepository)..add(GetTodoList()),
                      child: TodoListPage())),
              (route) => false);
        } else {
          Navigator.pushAndRemoveUntil(
              context,
              CupertinoPageRoute(
                  builder: (context) => BlocProvider(
                      create: (BuildContext context) => LoginBloc(),
                      child: LoginPage())),
              (route) => false);
        }
      });
    });
  }
}
