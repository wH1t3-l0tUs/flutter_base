import 'package:flutterbloc/model/todo_item.dart';
import 'package:meta/meta.dart';

@immutable
abstract class TodoEvent {}

class AddTodo extends TodoEvent {
  final TodoItem todoItem;

  AddTodo(this.todoItem);
}

class UpdateTodo extends TodoEvent {
  final TodoItem todoItem;

  UpdateTodo(this.todoItem);
}

class GetTodoList extends TodoEvent {}

class GetDoneList extends TodoEvent {}
