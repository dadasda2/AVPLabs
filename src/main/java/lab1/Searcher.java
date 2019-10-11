
//toDo сделать с произвольным пулом потоков

package lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;




public class Searcher<T> extends RecursiveTask<T>{
    private List<T> list;
    volatile T searchedVal;
    volatile T result;

    public Searcher(T val, List<T> ar){
        searchedVal = val;
        list = ar;
    }

    @Override
   protected T compute() {
        if(list.size() > 1){
            List<Searcher<T>> subTasks = new ArrayList<>();
            subTasks.addAll(createSubTasks());

            for(Searcher<T> subTask: subTasks){
                subTask.fork();
            }

            for(Searcher<T> subtask: subTasks){
                T temp = subtask.join();
                if(temp == this.searchedVal) {
                    this.result = temp;
                }
            }

            return result;
        }else {
            return list.get(0);
        }
    }

    public List<Searcher<T>> createSubTasks() {
        List<Searcher<T>> subtasks = new ArrayList<>();

        Searcher<T> subtask1 = new Searcher<T>(this.searchedVal, this.list.subList(0,list.size()/2));
        Searcher<T> subtask2 = new Searcher<T>(this.searchedVal, this.list.subList(list.size()/2,list.size()));

        subtasks.add(subtask1);
        subtasks.add(subtask2);

        return subtasks;

    }

    public static void main(String[] args) {

        ArrayList<Integer> ar = new ArrayList<Integer>();
        for(int i = 0; i < 30; i++){
            ar.add(i);
        }

        Searcher<Integer> a = new Searcher<Integer>(5, ar);
        ForkJoinPool fjp = new ForkJoinPool(10);
        Integer ll = fjp.invoke(a);

        System.out.printf("res = " + ll);
    }
}
