import 'package:flutterbloc/model/todo_item.dart';
import 'package:meta/meta.dart';

@immutable
abstract class TodoState {}

class TodoInit extends TodoState {}

class TodoLoading extends TodoState {}

class TodoLoaded extends TodoState {
  final List<TodoItem> listTodoItem;

  TodoLoaded(this.listTodoItem);
}
