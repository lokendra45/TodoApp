package com.tbcmad.todoapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.icu.util.EthiopicCalendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Validator;
import com.tbcmad.todoapp.model.ETodo;
import com.tbcmad.todoapp.viewModel.TodoViewModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListTodoFragment extends Fragment  {
    View rootView;
    RecyclerView rvListTodo;
    TodoViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_list_todo, container, false);
        rvListTodo = rootView.findViewById(R.id.list_item_todo_rv_list_todo);
        viewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        LinearLayoutManager manager= new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvListTodo.setLayoutManager(manager);
        updateRV();
        swipeToDelete();

        return  rootView;
    }
    private  void swipeToDelete(){
        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setCancelable(false);
                        builder.setMessage("Are You sure want to Delete Todo");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                List<ETodo> todoList = viewModel.getAllTodos().getValue();
                                TodoAdaptor adaptor = new TodoAdaptor(todoList);
                                ETodo todo = adaptor.getTodoAt(viewHolder.getAdapterPosition());
                                viewModel.deleteById(todo);
                                adaptor.notifyDataSetChanged();
                                dialog.cancel();
                            };
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                updateRV();
                                dialog.cancel();

                            };
                        }).create().show();

                    }

                }).attachToRecyclerView(rvListTodo);
    }

    void updateRV(){
        viewModel.getAllTodos().observe(getViewLifecycleOwner(), new Observer<List<ETodo>>() {
            @Override
            public void onChanged(List<ETodo> eTodos) {
                TodoAdaptor adaptor = new TodoAdaptor(eTodos);
                rvListTodo.setAdapter(adaptor);

            }
        });
    }

    private class TodoHolder extends RecyclerView.ViewHolder{
        TextView title, date, descriptions;
        CheckedTextView checkedTextView;
          CheckBox chComplete;
        public TodoHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_todo, parent, false));
            title = itemView.findViewById(R.id.list_item_tv_title);
            descriptions = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.list_item_tv_date);
            checkedTextView = itemView.findViewById(R.id.checkedTextView);
           chComplete = itemView.findViewById(R.id.edit_fragment_chk_complete);


            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadUpdateItem();
                }
            });
            descriptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadUpdateItem();
                }
            });{

            }
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadUpdateItem();
                }
            });
checkedTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadUpdateItem();
                }
            });


        }

       public void loadUpdateItem(){
            TodoAdaptor adaptor = new TodoAdaptor(viewModel.getAllTodos().getValue());
            int i = getAdapterPosition();
            ETodo todo = adaptor.getTodoAt(i);
            Intent intent = new Intent(getActivity(), EditActivity.class);
            intent.putExtra("TodoId",todo.getId());
            startActivity(intent);
            Toast.makeText(getContext(),"Update Item: " + todo.getId(), Toast.LENGTH_LONG).show();

        }
        public void bind(ETodo todo){
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            title.setText(todo.getTitle());
            descriptions.setText(todo.getDescription());
            date.setText(sdf.format(todo.getTodoDate()));
            if(todo.isCompleted()){
//
                checkedTextView.setText(R.string.task_Completed);
                strikeWord();


            }else{
                checkedTextView.setText(R.string.task_not_Completed);
            }






        }
        private  void strikeWord(){
            if(!checkedTextView.getPaint().isStrikeThruText()){
                checkedTextView.setPaintFlags(checkedTextView.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                title.setPaintFlags(checkedTextView.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                date.setPaintFlags(checkedTextView.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                descriptions.setPaintFlags(checkedTextView.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

            }



        }
    }

    private class TodoAdaptor extends RecyclerView.Adapter<TodoHolder>{
        List<ETodo> eTodoList;
        public TodoAdaptor(List<ETodo> todoList)
        {
            eTodoList = todoList;
        }

        @NonNull
        @Override
        public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TodoHolder(layoutInflater, parent);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull TodoHolder holder, int position) {


            ETodo todo = eTodoList.get(position);
            LinearLayout layout =(LinearLayout)((ViewGroup)holder.title.getParent());

            switch (todo.getPriority()) {
                case 1:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_high));

                    break;
                case 2:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_medium));
                    break;
                case 3:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_low));
                    break;

            }

            holder.bind(todo);

        }

        @Override
        public int getItemCount() {
            return eTodoList.size();
        }

        public ETodo getTodoAt(int position){
            return eTodoList.get(position);
        }

    }
}