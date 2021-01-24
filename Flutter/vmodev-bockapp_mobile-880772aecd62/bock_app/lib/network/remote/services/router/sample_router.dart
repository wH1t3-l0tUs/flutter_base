import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:dio/src/response.dart';

import '../api_client.dart';

enum EndPoint {
  //Here is where you put config what API you want to call
  getEmployee
}

class SampleRouter extends Service implements BaseRouter {
  EndPoint point;

  Map<String, dynamic> params;

  SampleRouter(this.point, this.params);

  @override
  bool isLoading() {
    return true;
  }

  @override
  Map<String, dynamic> headerParams() {
    Map<String, dynamic> map = <String, dynamic>{};
//    map["Authorization"] = "Bearer " + Global.storage.getString("apiAuthKey");
    return map;
  }

  @override
  Future<Response> call() {
    switch (point) {
      case EndPoint.getEmployee:
        return dio().get(path());
      default:
        return null;
    }
  }

  @override
  String path() {
    switch (point) {
      case EndPoint.getEmployee:
        return "/api/v1/employees";
    }
  }

  @override
  Future<Response<T>> request<T>() {
    switch (point) {
      case EndPoint.getEmployee:
        return dio().get(path());
    }
  }
}
