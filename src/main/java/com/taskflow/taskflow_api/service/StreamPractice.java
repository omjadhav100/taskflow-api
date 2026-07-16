package com.taskflow.taskflow_api.practice;

public class StreamPractice {

    public static void main(String[] args) {
List<String> titles=new ArrayList<>();
for(Task task:tasks){
    if(task.getStatus().equals("DONE")){
        titles.add(task.getTitles());
    }
}
List<String> titles = tasks.stream()
    .filter(task -> task.getStatus().equals("DONE"))
    .map(Task::getTitle)
    .collect(Collectors.toList());
}
}
