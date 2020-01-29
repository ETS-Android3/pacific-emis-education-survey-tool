import 'package:built_collection/built_collection.dart';
import 'package:hive/hive.dart';
import 'package:pacific_dashboards/data/database/database.dart';
import 'package:pacific_dashboards/data/database/model/teachers/hive_teacher.dart';
import 'package:pacific_dashboards/models/emis.dart';
import 'package:pacific_dashboards/models/teachers/teacher.dart';

class HiveTeachersDao extends TeachersDao {
  static const _kKey = 'teachers';

  static Future<T> _withBox<T>(Future<T> action(Box<List> box)) async {
    final Box<List> box = await Hive.openBox(_kKey);
    final result = await action(box);
    await box.close();
    return result;
  }

  @override
  Future<BuiltList<Teacher>> get(Emis emis) async {
    final storedTeachers = await _withBox((box) async => box.get(emis.id));
    if (storedTeachers == null) {
      return null;
    }
    List<Teacher> storedItems = [];
    for (var value in storedTeachers) {
      final hiveTeacher = value as HiveTeacher;
      storedItems.add(hiveTeacher.toTeacher());
    }
    return storedItems.build();
  }

  @override
  Future<void> save(BuiltList<Teacher> teachers, Emis emis) async {
    final hiveTeachers = teachers
        .map((it) => HiveTeacher.from(it)
          ..timestamp = DateTime.now().millisecondsSinceEpoch)
        .toList();

    await _withBox((box) async => box.put(emis.id, hiveTeachers));
  }
}
