import 'package:flutterbloc/model/todo_item.dart';
import 'package:sembast/sembast.dart';

class TodoRepository {
  Future<List<TodoItem>> getTodoList() async {
    List<TodoItem> listTodo = [];
    listTodo = await TodoItem().getModelWithCondition(
        Finder(filter: Filter.equals("isFinish", false)));
    return listTodo;
  }

  Future<List<TodoItem>> getDoneList() async {
    List<TodoItem> listDone = [];
    listDone = await TodoItem()
        .getModelWithCondition(Finder(filter: Filter.equals("isFinish", true)));
    return listDone;
  }

  Future addTodoItem(TodoItem item) async {
    var result = await TodoItem().insertModel(item.toJson());
  }

  Future finishTodo(TodoItem item) {
    TodoItem newItem = item..isFinish = true;
    newItem.updateModel(Finder(filter: Filter.equals("index", newItem.index)),
        newItem.toJson());
  }
}
