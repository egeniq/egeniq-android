package com.egeniq.app;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.egeniq.app.Controller.State;

/**
 * Fragment base.
 */
@TargetApi(11)
public class Fragment extends android.app.Fragment {
    private final ArrayList<Controller> _controllers = new ArrayList<Controller>();
    private State _state = State.INITIAL;
    
    /**
     * Add controller.
     * 
     * @param controller controller
     */
    public void addController(Controller controller) {
        _controllers.add(controller);
        controller.setFragment(this);
        controller.transitionTo(_state);
    }
    
    /**
     * Remove controller.
     * 
     * @param controller controller
     */
    public void removeController(Controller controller) {
        controller.transitionTo(State.DETACHED);
        controller.setFragment(null);        
        _controllers.remove(controller);
    }
    
    /**
     * On attach.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _transitionTo(State.ATTACHED);
    }    
    
    /**
     * On create.
     */
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _transitionTo(State.CREATED);
    }

    /**
     * Create view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _transitionTo(State.VIEW_CREATED);
        return null;
    }

    /**
     * On start.
     */
    @Override
    public void onStart() {
        super.onStart();
        _transitionTo(State.STARTED);
    }

    /**
     * On resume.
     */
    @Override
    public void onResume() {
        super.onResume();
        _transitionTo(State.RESUMED);
    }

    /**
     * On pause.
     */
    @Override
    public void onPause() {
        super.onPause();
        _transitionTo(State.PAUSED);
    }

    /**
     * On stop.
     */
    @Override
    public void onStop() {
        super.onStop();
        _transitionTo(State.STOPPED);
    }

    /**
     * On destroy view.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _transitionTo(State.VIEW_DESTROYED);
    }

    /**
     * On destroy.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        _transitionTo(State.DESTROYED);
    }  
    
    /**
     * On detach.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        _transitionTo(State.DETACHED);
    }      
    
    /**
     * Transition to the given state.
     * 
     * @param state New state.
     */
    private void _transitionTo(State state) {
        _state = state;
        
        for (Controller controller : _controllers) {
            controller.transitionTo(state);
        }        
    }
}