import 'package:flutterbloc/model/todo_item.dart';

class GenerateAutoIncrease {
  int generateNumberAutoIncrease(List<TodoItem> todoList) {
    var autoIncreaseNumber = 0;
    if (todoList.isEmpty) {
      autoIncreaseNumber = 0;
    } else {
      autoIncreaseNumber = todoList.last.index + 1;
    }
    return autoIncreaseNumber;
  }
}
