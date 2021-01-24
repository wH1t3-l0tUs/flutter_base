import 'package:meta/meta.dart';

@immutable
abstract class LoginEvent {}

class LoginPressed extends LoginEvent {
  final String email;
  final String password;

  LoginPressed(this.email, this.password);
}
