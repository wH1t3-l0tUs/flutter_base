import 'dart:async';
import 'package:bloc/bloc.dart';
import 'package:flutterbloc/model/todo_item.dart';
import 'package:flutterbloc/repository/todo_repository.dart';
import './bloc.dart';

class TodoBloc extends Bloc<TodoEvent, TodoState> {
  final TodoRepository todoRepository;

  TodoBloc(this.todoRepository) : super(TodoInit());

  @override
  Stream<TodoState> mapEventToState(
    TodoEvent event,
  ) async* {
    if (event is GetTodoList) {
      yield TodoLoading();
      List<TodoItem> listTodo = await todoRepository.getTodoList();
      await Future.delayed(const Duration(seconds: 1));
      yield TodoLoaded(listTodo);
    } else if (event is GetDoneList) {
      yield TodoLoading();
      List<TodoItem> listTodo = await todoRepository.getDoneList();
      await Future.delayed(const Duration(seconds: 1));
      yield TodoLoaded(listTodo);
    } else if (event is AddTodo) {
      yield TodoLoading();
      await todoRepository.addTodoItem(event.todoItem);
      await Future.delayed(const Duration(seconds: 1));
      List<TodoItem> listTodo = await todoRepository.getTodoList();
      yield TodoLoaded(listTodo);
    } else if (event is UpdateTodo) {
      yield TodoLoading();
      await todoRepository.finishTodo(event.todoItem);
      await Future.delayed(const Duration(seconds: 1));
      List<TodoItem> listTodo = await todoRepository.getTodoList();
      yield TodoLoaded(listTodo);
    }
  }
}
