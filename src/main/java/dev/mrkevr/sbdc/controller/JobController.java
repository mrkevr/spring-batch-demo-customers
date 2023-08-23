package dev.mrkevr.sbdc.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/jobs")
public class JobController {

	JobLauncher jobLauncher;
	Job job;
	
	String BATCH_FILES_PATH = "D:\\Programming Applications\\eclipse-workspace\\spring-batch-demo-customers\\src\\main\\resources\\batch-files\\";
	
	@PostMapping("/importCustomers")
    public void importCsvToDBJob(
			@RequestParam(name = "file", required = true) MultipartFile multipartFile) {
		
        try {
        	
			
			File fileToRead = this.upload(multipartFile);
			
            JobParameters jobParameters = new JobParametersBuilder()
            		.addString("fullPathFileName", fileToRead.getAbsolutePath())
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
        	
            jobLauncher.run(job, jobParameters);
            
//			if (jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
//				// Delete or process the file
//				Path path = Paths.get(BATCH_FILES_PATH + originalFilename);
//				Files.deleteIfExists(path);
//			}
            
        } catch (
        		JobExecutionAlreadyRunningException | 
        		JobRestartException | 
        		JobInstanceAlreadyCompleteException | 
        		JobParametersInvalidException | 
        		IllegalStateException | 
        		IOException e) {
            e.printStackTrace();
        }
    }
	
	// Helper method to upload the File
	private File upload(MultipartFile multipartFile) throws IllegalStateException, IOException {
		// Create a File
    	Date date = new Date();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String newFileName = UUID.randomUUID().toString() + "_" + dateFormat.format(date);
		
		// The file name is random UUID + Date and time it is uploaded
		File fileToRead = new File(BATCH_FILES_PATH + newFileName + ".csv");
		
		// Transfer the MultipartFile to the File
		multipartFile.transferTo(fileToRead);
		return fileToRead;
	}
	
	
	
	
}