import 'dart:async';
import 'package:bloc/bloc.dart';
import 'package:flutterbloc/config/global.dart';
import './bloc.dart';

class LoginBloc extends Bloc<LoginEvent, LoginState> {
  LoginBloc() : super(LoginInit());

  @override
  Stream<LoginState> mapEventToState(
    LoginEvent event,
  ) async* {
    if (event is LoginPressed) {
      yield LoginLoading();
      try {
        await Future.delayed(const Duration(seconds: 3));
        if (event.email == Global.emailDefault &&
            event.password == Global.passwordDefault) {
          yield LoginFinish(event.email);
        } else {
          yield LoginError("Invalid email or password");
        }
      } catch (e) {
        yield LoginError("Have error when login");
      }
    }
  }
}
