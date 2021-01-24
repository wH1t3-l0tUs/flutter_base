import 'dart:async';

import 'package:flutterbloc/utils/validators/validate_login.dart';
import 'package:rxdart/rxdart.dart';

class ValidateBloc extends Object with ValidateLogin {
  final StreamController _emailController = BehaviorSubject<String>();
  final StreamController _passwordController = BehaviorSubject<String>();

  Stream<String> get email => _emailController.stream.transform(validateEmail);

  Stream<String> get password =>
      _passwordController.stream.transform(validatePassword);

  Stream<bool> get submitValid =>
      Rx.combineLatest2(email, password, (a, b) => true);

  void inputEmail(String email) {
    _emailController.sink.add(email);
  }

  void inputPassword(String password) {
    _passwordController.sink.add(password);
  }

  void dispose() {
    _emailController.close();
    _passwordController.close();
  }
}

final validateBloc = ValidateBloc();
