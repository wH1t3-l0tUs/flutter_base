import 'dart:async';

class ValidateLogin {
  final validateEmail =
      StreamTransformer<String, String>.fromHandlers(handleData: (email, sink) {
    if (email.contains("@")) {
      sink.add(email);
    } else {
      sink.addError("Enter a valid email");
    }
  });

  final validatePassword = StreamTransformer<String, String>.fromHandlers(
      handleData: (password, sink) {
    if (password.length >= 6) {
      sink.add(password);
    } else {
      sink.addError("Password must be more than 6");
    }
  });
//  bool validateEmail(String email) {
//    if (email.contains("@")) {
//      return true;
//    } else {
//      return false;
//    }
//  }

//  bool validatePassword(String password) {
//    if (password.length >= 6) {
//      return true;
//    } else {
//      return false;
//    }
//  }
}
