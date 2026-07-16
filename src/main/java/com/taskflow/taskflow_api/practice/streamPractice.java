package com.taskflow.taskflow_api.practice;

public class StreamPractice {

    public static void main(String[] args) {

        // Practice all Stream and Lambda code here
        Task->task.getTitle()
        Task->{return task.getTitle();}
        Task:getTitle
    List<Task>
    long doneCount=tasks.stream()
    .filter(t->.getstatus().equals("done"))
    .count();

    //stream chaining
    List<String> overdueTitlesSorted = tasks.stream()
    .filter(t -> t.getDueDate().isBefore(LocalDate.now()))   // only overdue
    .sorted(Comparator.comparing(Task::getDueDate))            // earliest first
    .map(Task::getTitle)                                        // just the titles
    .collect(Collectors.toList());
    }
}