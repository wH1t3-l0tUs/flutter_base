import 'package:flutterbloc/database/app_database.dart';
import 'package:flutterbloc/model/base_model.dart';
import 'package:json_annotation/json_annotation.dart';
import 'package:sembast/src/api/finder.dart';
part 'todo_item.g.dart';

@JsonSerializable(nullable: false)
class TodoItem extends BaseModel {
  int index;
  String note;
  bool isFinish;

  TodoItem({this.index, this.note, this.isFinish});

  factory TodoItem.fromJson(Map<String, dynamic> json) =>
      _$TodoItemFromJson(json);
  Map<String, dynamic> toJson() => _$TodoItemToJson(this);

  @override
  Future deleteModel(Finder finder) async {
    return await AppDatabase.delete(AppDatabase.todoDatabaseName, finder);
  }

  @override
  Future deleteModelAll() async {
    return await AppDatabase.deleteAll(AppDatabase.todoDatabaseName);
  }

  @override
  Future getModel() async {
    final model = await AppDatabase.getAll(AppDatabase.todoDatabaseName);
    return model.map((snapshot) {
      // print(snapshot.key);
      return TodoItem.fromJson(snapshot.value);
    }).toList();
  }

  @override
  Future insertModel(Map<String, dynamic> map) async {
    return await AppDatabase.insert(AppDatabase.todoDatabaseName, map);
  }

  @override
  Future insertModelAll(List<Map<String, dynamic>> mapList) async {
    return await AppDatabase.insertAll(AppDatabase.todoDatabaseName, mapList);
  }

  @override
  Future updateModel(Finder finder, Map<String, dynamic> map) async {
    return await AppDatabase.update(AppDatabase.todoDatabaseName, finder, map);
  }

  @override
  Future updateModelAll(Map<String, dynamic> map) async {
    return await AppDatabase.updateAll(AppDatabase.todoDatabaseName, map);
  }

  @override
  Future getModelWithCondition(Finder finder) async {
    final model = await AppDatabase.getAllWithCondition(
        AppDatabase.todoDatabaseName, finder);
    return model.map((snapshot) {
      return TodoItem.fromJson(snapshot.value);
    }).toList();
  }
}
