package com.common.point.service;

import com.common.point.exception.ErrorCode;
import com.common.point.exception.PointServerException;
import com.common.point.model.dto.PointUseFailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

	private final KafkaTemplate<String, PointUseFailMessage> kafkaTemplate;

	public boolean sendMessagePointSystem(PointUseFailMessage pointUseFailMessage) {
		boolean result = false;
		String topic = "point-system";
		String key = pointUseFailMessage.getCompanyNo().toString();

		try{
			kafkaTemplate.send(topic, pointUseFailMessage);
			SendResult<String, PointUseFailMessage> sendResult = kafkaTemplate.send("point-system", key, pointUseFailMessage).get();
			log.error("Message sent successfully to topic: {}, partition: {}, offset: {}",
                                      sendResult.getRecordMetadata().topic(),
                                      sendResult.getRecordMetadata().partition(),
                                      sendResult.getRecordMetadata().offset());
			result = true;
		}catch(Exception e){
			log.error("Failed to send message to Kafka. Reason: {}", e.getMessage(), e);
			throw new PointServerException(ErrorCode.POINT_UPDATE_FAILED_AND_KAFKA_MESSAGE_RETRY_FAILED);
		}
		return result;
    }
}
