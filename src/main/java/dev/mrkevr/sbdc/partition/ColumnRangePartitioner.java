package dev.mrkevr.sbdc.partition;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class ColumnRangePartitioner implements Partitioner {

	private final int MIN = 1;
	private final int MAX = 1000;

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {

		Map<String, ExecutionContext> map = new HashMap<>();
		int targetSize = (MAX - MIN) / gridSize + 1;

		int counter = 0;
		int start = MIN;
		int end = start + targetSize - 1;

		while (start <= MAX) {
			ExecutionContext context = new ExecutionContext();
			map.put("partition_" + counter, context);

			if (end >= MAX) {
				end = MAX;
			}

			context.putInt("minValue", start);
			context.putInt("maxValue", end);

			start += targetSize;
			end += targetSize;
			counter++;
		}

		return map;
	}

}
