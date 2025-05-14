
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class TimerController {
	
	
	// Text fields to get input from the user
	@FXML
	private TextField inputHours;
	
	@FXML
	private TextField inputMinutes;
	
	@FXML
	private TextField inputSeconds;
	
	// Label to display the remaining time
	@FXML
	private Label display;
	
	// ProgressBar to visually indicate the progress
	@FXML
	private ProgressBar progressBar;
	
	// Remaining seconds in the countdown timer
	private int remainingSeconds;
	
	// Stores the initial total seconds to calculate progress ratio
	private int secondsProgress;
	
	// Timeline object that runs the timer update every second
	private Timeline timeline;
	
	@FXML
	private void start(ActionEvent event) {
		
		try {
			// Parse input hours, minutes, seconds; default to 0 if empty
			int hours = inputHours.getText().isEmpty() ? 0 : Integer.parseInt(inputHours.getText());
			int minutes = inputMinutes.getText().isEmpty() ? 0 : Integer.parseInt(inputMinutes.getText());
			int seconds = inputSeconds.getText().isEmpty() ? 0 : Integer.parseInt(inputSeconds.getText());
			
			// Convert total input time to seconds
			remainingSeconds = hours * 3600 + minutes * 60 + seconds;
			secondsProgress = remainingSeconds;
			
			//If input time is zero or negative, show error and return
	        if (remainingSeconds <= 0) {
	        	display.setText("Invalid.");
	            return;
	        }
			
	        // If a timer is already running, stop it before starting a new one
            if (timeline != null) {
                timeline.stop();
            }
            
            // Create a new timeline that triggers every second
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                if (remainingSeconds > 0) {
                	remainingSeconds--;     // Decrement seconds left
                    updateLabel();          // Update displayed time
                    updateProgress();		// Update progress bar
                } else {
                	display.setText("Done!"); //Timer finished message
                    timeline.stop(); 		  // Stop timeline when done
                }
            }));
            
            // Run the timeline indefinitely until stopped and start
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            
            // Initial update of label and progress bar
            updateLabel();
            updateProgress();
            
        } catch (NumberFormatException e) {
            // Show error message if input is not a valid number
        	display.setText("Wrong Input!");
        }
    } 
	
    // Method called when Pause button is clicked to pause the timer
	@FXML
	private void pause(ActionEvent event) {
		
		if (timeline != null) {
			timeline.pause(); // If the timeline exists, it gets paused.
		}
		
	}
	
    // Method called when Reset button is clicked to reset everything
	@FXML
	private void reset(ActionEvent event) {
		
		progressBar.setProgress(0);
	    timeline.stop();
	    display.setText("00:00:00");
		inputHours.clear();
		inputMinutes.clear();
		inputSeconds.clear();
		
	}
	
    // Helper method to update the time display label in HH:mm:ss format
    private void updateLabel() {
    	
        int h = remainingSeconds / 3600;
        int m = (remainingSeconds % 3600) / 60;
        int s = remainingSeconds % 60;

        String time = String.format("%02d:%02d:%02d", h, m, s);
        display.setText(time);
        
    }
    
    // Helper method to update the progress bar based on remaining time
    private void updateProgress() {
    	
    	// Calculate fraction of time elapsed and update progress bar
    	double progress = (double) remainingSeconds / secondsProgress;
    	progressBar.setProgress(1 - progress); // Progress bar fills from left to right as time counts down
    	
    }
    
}
