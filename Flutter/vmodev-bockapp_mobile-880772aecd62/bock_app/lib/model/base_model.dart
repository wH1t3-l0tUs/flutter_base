import 'package:sembast/sembast.dart';

abstract class BaseModel {
  Future insertModel(Map<String, dynamic> map);
  Future insertModelAll(List<Map<String, dynamic>> mapList);
  Future getModel();

  // For filtering by key (ID), RegEx, greater than, and many other criteria,
  // we use a Finder.
  // Finder finder = new Finder(
  //   // filter: Filter.byKey(key)
  //   // filter: Filter.matches("cancelReasonID", "1")
  // );
  Future getModelWithCondition(Finder finder);

  Future updateModel(Finder finder, Map<String, dynamic> map);
  Future updateModelAll(Map<String, dynamic> map);
  Future deleteModel(Finder finder);
  Future deleteModelAll();
}
