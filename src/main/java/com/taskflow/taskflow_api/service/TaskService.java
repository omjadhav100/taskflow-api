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