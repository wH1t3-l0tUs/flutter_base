import 'package:path_provider/path_provider.dart';
import 'package:sembast/sembast.dart';
import 'package:sembast/sembast_io.dart';

class AppDatabase {
  // branch json from api
  static const String todoDatabaseName = "Todo";

  static Database db;

  static Future initDatabase() async {
    // Get a platform-specific directory where persistent app data can be stored
    final appDocumentDir = await getApplicationDocumentsDirectory();
    // Path with the form: /platform-specific-directory/database.db
    final dbPath = appDocumentDir.path + '/database.db';
    AppDatabase.db = await databaseFactoryIo.openDatabase(dbPath);
  }

  static Future insert(String nameDb, Map<dynamic, dynamic> map) async {
    return await intMapStoreFactory.store(nameDb).add(db, map);
  }

  static Future insertAll(
      String nameDb, List<Map<dynamic, dynamic>> map) async {
    return await intMapStoreFactory.store(nameDb).addAll(db, map);
  }

  static Future<List<RecordSnapshot<int, Map<String, dynamic>>>> getAll(
      String nameDb) async {
    return await intMapStoreFactory.store(nameDb).find(
          db,
        );
  }

  static Future<List<RecordSnapshot<int, Map<String, dynamic>>>>
      getAllWithCondition(String nameDb, Finder finder) async {
    return await intMapStoreFactory.store(nameDb).find(db, finder: finder);
  }

  static Future<RecordSnapshot<int, Map<String, dynamic>>> getLastItem(
      String nameDb, Finder finder) async {
    return await intMapStoreFactory.store(nameDb).findFirst(db, finder: finder);
  }

  static Future update(
      String nameDb, Finder finder, Map<dynamic, dynamic> map) async {
    // For filtering by key (ID), RegEx, greater than, and many other criteria,
    // we use a Finder.
    // Finder finder = new Finder(
    //   // filter: Filter.byKey(key)
    //   // filter: Filter.matches("cancelReasonID", "1")
    // );
    return await intMapStoreFactory.store(nameDb).update(
          db,
          map,
          finder: finder,
        );
  }

  static Future updateAll(String nameDb, Map<dynamic, dynamic> map) async {
    return await intMapStoreFactory.store(nameDb).update(
          db,
          map,
        );
  }

  static Future delete(String nameDb, Finder finder) async {
    return await intMapStoreFactory.store(nameDb).delete(
          db,
          finder: finder,
        );
  }

  static Future deleteAll(String nameDb) async {
    return await intMapStoreFactory.store(nameDb).drop(db);
  }

  static List<Map<String, dynamic>> asMapList(dynamic list) {
    return (list as List)?.cast<Map<String, dynamic>>();
  }
}
