import 'package:meta/meta.dart';

@immutable
abstract class LoginState {}

class LoginInit extends LoginState {}

class LoginLoading extends LoginState {}

class LoginFinish extends LoginState {
  final String userName;

  LoginFinish(this.userName);
}

class LoginError extends LoginState {
  final String error;

  LoginError(this.error);
}
