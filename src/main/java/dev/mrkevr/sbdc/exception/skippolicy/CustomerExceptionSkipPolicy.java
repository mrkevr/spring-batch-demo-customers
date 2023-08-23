package dev.mrkevr.sbdc.exception.skippolicy;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

@Component
public class CustomerExceptionSkipPolicy implements SkipPolicy {

	@Override
	public boolean shouldSkip(Throwable throwable, long skipCount) throws SkipLimitExceededException {
		return throwable instanceof NumberFormatException ||
			   throwable instanceof FlatFileParseException ||
			   throwable instanceof IllegalArgumentException;
	}

}
