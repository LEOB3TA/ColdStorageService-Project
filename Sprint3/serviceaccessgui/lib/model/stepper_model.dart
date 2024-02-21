import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final stepperFormKeys = List.generate(4, (index) => GlobalKey<FormState>());

class StepperModel {
  final int index;
  final List<bool> completed;

  const StepperModel({
    this.index = 0,
    required this.completed,
  });

  StepperModel copyWith({
    int? index,
    List<bool>? completed,
  }) {
    return StepperModel(
      index: index ?? this.index,
      completed: completed ?? this.completed,
    );
  }
}

class StepperNotifier extends AutoDisposeNotifier<StepperModel> {
  @override
  StepperModel build() => StepperModel(completed: List.generate(4, (index) => false));

  void setIndex(int index) => state = state.copyWith(index: index);

  void setCompleted(int index) {
    final completed = List.from(state.completed);
    completed[index] = true;
    state = state.copyWith(completed: List.unmodifiable(completed));
  }
}

final stepperProvider = NotifierProvider.autoDispose<StepperNotifier, StepperModel>(
  () => StepperNotifier(),
);
