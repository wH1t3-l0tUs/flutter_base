// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'todo_item.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

TodoItem _$TodoItemFromJson(Map<String, dynamic> json) {
  return TodoItem(
    note: json['note'] as String,
    isFinish: json['isFinish'] as bool,
  )..index = json['index'] as int;
}

Map<String, dynamic> _$TodoItemToJson(TodoItem instance) => <String, dynamic>{
      'index': instance.index,
      'note': instance.note,
      'isFinish': instance.isFinish,
    };
