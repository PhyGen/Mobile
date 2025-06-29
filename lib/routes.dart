// routes.dart
import 'package:flutter/material.dart';
import 'signin.dart';
import 'signup.dart';
import 'mainscreen.dart';

class AppRoutes {
  static const String signIn = '/signin';
  static const String signUp = '/signup';
  static const String home = '/home'; // tuỳ bạn thêm

  static Map<String, WidgetBuilder> get routes {
    return {
      signIn: (context) => const SignInPage(),
      signUp: (context) => const SignUpPage(),
      home: (context) => const MainScreen(), // ✅ Dùng MainScreen ở đây
    };
  }
}
