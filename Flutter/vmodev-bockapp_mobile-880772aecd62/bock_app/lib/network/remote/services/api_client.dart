import 'package:dio/dio.dart';
import 'package:flutterbloc/config/global.dart';

import 'router/sample_router.dart';

enum ServiceHeader { authenticate, custom, defaultHeader }

abstract class BaseService {
  Map<String, dynamic> headerParams();
}

class Service extends BaseService {
  @override
  Map<String, dynamic> headerParams() {
    // TODO: implement headerParams
    return null;
  }

  String get baseURL {
    var baseURL = Global.baseURL;
    print("Get Base URL API Client : " + baseURL);
    return baseURL;
  }

  Dio dio() {
    var dio = Dio();
    dio.options.baseUrl = baseURL;
    dio.options.connectTimeout = 60000; //5s
    dio.options.receiveTimeout = 60000;
    dio.options.contentType = Headers.jsonContentType;
    if (headerParams() != null) {
      dio.options.headers = headerParams();
//      dio.options.contentType = Headers.formUrlEncodedContentType;
    }
    _setupLoggingInterceptor(dio);
    return dio;
  }

  _setupLoggingInterceptor(Dio dio) {
    dio.interceptors
        .add(InterceptorsWrapper(onRequest: (RequestOptions options) {
      print("--> ${options.method} ${options.path} ${options.headers}");
      print("Content type: ${options.contentType}");
      print("Data: ${options.data}");
      print("<-- END HTTP -->");
      return options; //continue
    }, onResponse: (Response response) {
      // Do something with response data
      print(
          "<-- ${response.statusCode} ${response.request.method} ${response.request.path}");
      String responseAsString = response.data.toString();
      print(responseAsString);
      print("<-- END HTTP -->");
      return response; // continue
    }, onError: (DioError e) {
      // Do something with response error
      print(e.toString());
      return e; //continue
    }));
  }
}

abstract class BaseRouter {
  Future<Response<T>> request<T>();

  bool isLoading();

  String path();

  Future<Response> call();
}

class APIClient extends Service {
  static APIClient _shared;

  static APIClient get shared {
    _shared ??= APIClient();
    return _shared;
  }

  Future<Response<T>> request<T>(BaseRouter router) {
    return router.request();
  }

  Future<Response> call(BaseRouter router) {
    return router.call();
  }

  Future<Map<String, dynamic>> getEmployee(Map<String, dynamic> map) async {
    var router = await SampleRouter(EndPoint.getEmployee, map).call();
    return router.data;
  }
}
