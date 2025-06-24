// import 'package:flutter/material.dart';
// import 'home_box.dart';
// import 'my_exam.dart';
// import 'recently.dart';
// import 'trash_can.dart';
// import 'create_type_select.dart';
//
// class MainScreen extends StatefulWidget {
//   const MainScreen({super.key});
//
//   @override
//   State<MainScreen> createState() => _MainScreenState();
// }
//
// class _MainScreenState extends State<MainScreen> {
//   int _selectedIndex = 0;
//   String? _createType; // Đổi tên cho rõ ràng hơn
//
//   Danh sách các màn hình
//   final List<Widget> _screens = [
//     const HomeBox(),
//     const CreateTypeSelect(),
//     const MyExam(),
//     const Recently(),
//     const TrashCan(),
//   ];
//
//   // Danh sách tiêu đề
//   final List<String> _titles = [
//     'Home',
//     'Create',
//     'My Exam',
//     'Recently',
//     'Trash',
//   ];
//
//   // Xử lý khi chọn item
//   void _onItemTapped(int index) {
//     if (index >= 0 && index < _screens.length) {
//       setState(() {
//         _selectedIndex = index;
//       });
//     }
//   }
//
//   // Xây dựng placeholder với giao diện linh hoạt
//   Widget _buildPlaceholder(String title, IconData icon, String description, Widget child) {
//     return Card(
//       margin: const EdgeInsets.all(16),
//       elevation: 4,
//       child: Padding(
//         padding: const EdgeInsets.all(16),
//         child: Column(
//           mainAxisSize: MainAxisSize.min, // Giới hạn chiều cao
//           children: [
//             CircleAvatar(
//               backgroundColor: Colors.blue[100],
//               child: Icon(icon, size: 30, color: Colors.blue),
//               radius: 30,
//             ),
//             const SizedBox(height: 12),
//             Text(title, style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
//             const SizedBox(height: 8),
//             Text(description, style: const TextStyle(color: Colors.grey)),
//             const SizedBox(height: 16),
//             Expanded(child: child), // Đảm bảo child chiếm không gian còn lại
//           ],
//         ),
//       ),
//     );
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(
//         title: Text(_titles[_selectedIndex]),
//       ),
//       body: IndexedStack(
//         index: _selectedIndex,
//         children: _screens.map((screen) {
//           if (screen is MyExam) {
//             return _buildPlaceholder(
//               _titles[_screens.indexOf(screen)],
//               Icons.book,
//               'Manage your exams',
//               screen,
//             );
//           }
//           return screen; // Giữ nguyên các màn hình khác
//         }).toList(),
//       ),
//       bottomNavigationBar: BottomNavigationBar(
//         currentIndex: _selectedIndex,
//         onTap: _onItemTapped,
//         items: const [
//           BottomNavigationBarItem(icon: Icon(Icons.home), label: 'Home'),
//           BottomNavigationBarItem(icon: Icon(Icons.add), label: 'Create'),
//           BottomNavigationBarItem(icon: Icon(Icons.book), label: 'My Exam'),
//           BottomNavigationBarItem(icon: Icon(Icons.history), label: 'Recently'),
//           BottomNavigationBarItem(icon: Icon(Icons.delete), label: 'Trash'),
//         ],
//         type: BottomNavigationBarType.fixed, // Đảm bảo không tràn layout
//       ),
//     );
//   }
// }