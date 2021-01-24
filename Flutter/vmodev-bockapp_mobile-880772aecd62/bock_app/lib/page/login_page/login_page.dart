import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterbloc/config/global.dart';
import 'package:flutterbloc/page/todo_list_page/todo_bloc/bloc.dart';
import 'package:flutterbloc/page/todo_list_page/todo_list_page.dart';
import 'package:flutterbloc/repository/todo_repository.dart';

import 'login_bloc/bloc.dart';

class LoginPage extends StatefulWidget {
  LoginPage({Key key}) : super(key: key);

  @override
  _LoginPageState createState() {
    return _LoginPageState();
  }
}

class _LoginPageState extends State<LoginPage> {
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    emailController.dispose();
    passwordController.dispose();
    validateBloc.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final LoginBloc loginBloc = BlocProvider.of<LoginBloc>(context);

    return SafeArea(
      child: Scaffold(
        body: BlocConsumer<LoginBloc, LoginState>(
          listener: (context, state) {
            if (state is LoginFinish) {
              var todoRepository = TodoRepository();

              Global.storage.putBool("isLogin", true);
              Navigator.pushAndRemoveUntil(
                  context,
                  CupertinoPageRoute(
                      builder: (context) => BlocProvider(
                          create: (context) =>
                              TodoBloc(todoRepository)..add(GetTodoList()),
                          child: TodoListPage())),
                  (route) => false);
            } else if (state is LoginError) {
              Scaffold.of(context).showSnackBar(SnackBar(
                content: Text(state.error),
              ));
            }
          },
          builder: (context, state) {
            if (state is LoginInit) {
              return buildInitLogin(loginBloc);
            } else if (state is LoginLoading) {
              return const Center(
                child: CircularProgressIndicator(),
              );
            } else {
              return buildInitLogin(loginBloc);
            }
          },
        ),
      ),
    );
  }

  Widget buildInitLogin(LoginBloc loginBloc) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          StreamBuilder<String>(
              stream: validateBloc.email,
              builder: (context, snapshot) {
                return TextField(
                  onChanged: (newValue) {
                    validateBloc.inputEmail(newValue);
                  },
                  controller: emailController,
                  keyboardType: TextInputType.emailAddress,
                  decoration: InputDecoration(
                      hintText: "Enter Your Email",
                      labelText: "Email",
                      isDense: true,
                      contentPadding: const EdgeInsets.all(10.0),
                      errorText: snapshot.error,
                      border: const OutlineInputBorder(
                          borderRadius:
                              BorderRadius.all(Radius.circular(16.0)))),
                );
              }),
          const SizedBox(
            height: 30.0,
          ),
          StreamBuilder<String>(
              stream: validateBloc.password,
              builder: (context, snapshot) {
                return TextField(
                  onChanged: (newValue) {
                    validateBloc.inputPassword(newValue);
                  },
                  obscureText: true,
                  controller: passwordController,
                  decoration: InputDecoration(
                      hintText: "Enter Your Password",
                      isDense: true,
                      labelText: "Password",
                      contentPadding: const EdgeInsets.all(10.0),
                      errorText: snapshot.error,
                      border: const OutlineInputBorder(
                          borderRadius:
                              BorderRadius.all(Radius.circular(16.0)))),
                );
              }),
          const SizedBox(
            height: 30.0,
          ),
          StreamBuilder<bool>(
              stream: validateBloc.submitValid,
              builder: (context, snapshot) {
                return RaisedButton(
                  color: Colors.blue,
                  onPressed: snapshot.hasData
                      ? () {
                          loginBloc.add(LoginPressed(
                              emailController.text, passwordController.text));
                        }
                      : null,
                  shape: const RoundedRectangleBorder(
                      borderRadius: BorderRadius.all(Radius.circular(32.0))),
                  child: Text(
                    "LOGIN",
                    style: AppTextStyle.whiteLargeText,
                  ),
                );
              })
        ],
      ),
    );
  }
}
