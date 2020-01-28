package th.co.scb.fasteasy.LogcollectorBackend.Controller;


import th.co.scb.fasteasy.LogcollectorBackend.Elastic.ElasticseachUtil;
import th.co.scb.fasteasy.LogcollectorBackend.Model.Field;
import th.co.scb.fasteasy.LogcollectorBackend.Model.Models;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class FieldController {
    @Autowired
    JobLauncher jobLauncher;
    @Qualifier("JobSave")
    @Autowired
    Job jobSave;
    @Qualifier("JobGet")
    @Autowired
    Job JobGetField;
    @Autowired
    private ElasticseachUtil elasticseachUtil;


    @Autowired
    private Models models;
    ObjectMapper mapper = new ObjectMapper();
    String[] audits = {"@timestamp","@version","ACCESS_ID","ACCOUNT_FROM","ACCOUNT_FROM_NAME",
            "ACCOUNT_TO_NAME","ACCOUNT_TYPE","ACCURACY","ADDITIONAL_DATA","AMOUNT",
            "APP_VERSION","BANK_CODE","BARCODE_STRING","BARCODE_TYPE","BILLER_CATEGORY",
            "BILL_PAYMENT_TYPE","BP_REF","BRANCH_ACCOUNT_FROM","BRAND","CHANNEL","CORRELATION_ID",
            "COUNTRY_CODE","CUSTOMER_LANGUAGE","CUSTOMER_TYPE_CODE","DEVICE_ID","EVENT_CODE",
            "FEE","FEE2","FEE3","FUNCTION_NAME","IP","KEY_IN_REF1","KEY_IN_REF2","KEY_IN_REF3",
            "LATITUDE","LOGIN_STATUS","LONGITUDE","MERCHANT_NAME","MINI_QR","MODEL","MODEL_NO",
            "OC_NUMBER_FROM","OTA_EXPIRE_TIME","OTA_MOBILE_NO","PARTNER_ID","PARTNER_NAME",
            "PAYMENT_REF1","PAYMENT_REF2","PAYMENT_REF3","PREVIOUS_LOGIN","REF_ID"
            ,"REGISTRATION_DATE","REQUEST_ID","RESPONSE_CODE","RESPONSE_DESC","SCAN_TOKEN",
            "SCB_CHANNEL","SENDER_BANK","SENDER_TYPE","SERVICE_NUMBER","SERVICE_NUMBER_TYPE",
            "STATUS","SUB_EVENT_CODE","TC_VERSION","TIMESTAMP","TRANSACTION_SENT_TIME","UPDATED_BY",
            "USER_ID","type"};
    String[] ferequsetlogs = {"@timestamp","@version","api_command","api_correlationid","api_method","api_oauth_access",
            "api_oauth_scope","api_response_status","api_source","api_span","api_trace","api_uri","api_userid",
            "channel","clientIp","client_app_build","client_app_name","client_app_version","client_os_name",
            "client_os_version","client_user-agent","contentType","contextPath","executeTime","id","kafkaOffset",
            "kafkaReceivedPartitionId","kafkaReceivedTopic","localAddr","method","msgBodyLength",
            "msgHeaders_Content-Type","msgHeaders_X-B3-ParentSpanId","msgHeaders_X-B3-Sampled","msgHeaders_X-B3-SpanId",
            "msgHeaders_X-B3-TraceId","msgHeaders_accept","msgHeaders_accept-encoding","msgHeaders_accept-language",
            "msgHeaders_access-control-allow-origin","msgHeaders_accessId","msgHeaders_accessid","msgHeaders_accuracy",
            "msgHeaders_api-auth","msgHeaders_api-scope","msgHeaders_baseuri","msgHeaders_channelid","msgHeaders_connection",
            "msgHeaders_content-length","msgHeaders_correlationid","msgHeaders_deliveryAttempt","msgHeaders_host",
            "msgHeaders_id","msgHeaders_kafka_offset","msgHeaders_kafka_receivedPartitionId","msgHeaders_kafka_receivedTimestamp",
            "msgHeaders_kafka_receivedTopic","msgHeaders_kafka_timestampType","msgHeaders_latitude","msgHeaders_longitude",
            "msgHeaders_mq-rquid","msgHeaders_nativeHeaders_X-B3-ParentSpanId","msgHeaders_nativeHeaders_X-B3-Sampled",
            "msgHeaders_nativeHeaders_X-B3-SpanId","msgHeaders_nativeHeaders_X-B3-TraceId","msgHeaders_nativeHeaders_baseuri",
            "msgHeaders_nativeHeaders_correlationid","msgHeaders_nativeHeaders_spanId","msgHeaders_nativeHeaders_spanParentSpanId",
            "msgHeaders_nativeHeaders_spanSampled","msgHeaders_nativeHeaders_spanTraceId","msgHeaders_parent-rquid",
            "msgHeaders_rquid","msgHeaders_scb-channel","msgHeaders_source","msgHeaders_spanId","msgHeaders_spanParentSpanId",
            "msgHeaders_spanSampled","msgHeaders_spanTraceId","msgHeaders_th_co_scb-easy-rquid","msgHeaders_th_co_scb-easy-sessionid",
            "msgHeaders_timestamp","msgHeaders_user-agent","msgHeaders_userId","msgHeaders_userMode","msgHeaders_userid",
            "msgHeaders_x-forwarded-for","msgHeaders_x-forwarded-host","msgHeaders_x-forwarded-port","msgHeaders_x-forwarded-prefix",
            "msgHeaders_x-forwarded-proto","msgSent","parentSpanId","payee_bankCode","rawUri","remotePort",
            "remoteServer","reqBody","reqContentLength","reqHeaders_Accept","reqHeaders_Accept-Charset","reqHeaders_Content-Length",
            "reqHeaders_Content-Type","reqHeaders_X-B3-ParentSpanId","reqHeaders_X-B3-Sampled","reqHeaders_X-B3-SpanId",
            "reqHeaders_X-B3-TraceId","reqHeaders_accept","reqHeaders_accept-charset","reqHeaders_accept-encoding",
            "reqHeaders_accept-language","reqHeaders_access-control-allow-origin","reqHeaders_accessid","reqHeaders_accuracy",
            "reqHeaders_api-auth","reqHeaders_api-refresh","reqHeaders_api-scope","reqHeaders_apiKey","reqHeaders_apiSecret","reqHeaders_apikey",
            "reqHeaders_apisecret","reqHeaders_baseuri","reqHeaders_bearer","reqHeaders_channelid","reqHeaders_configversion",
            "reqHeaders_connection","reqHeaders_content-length","reqHeaders_content-type","reqHeaders_correlationid",
            "reqHeaders_elastic-apm-traceparent","reqHeaders_host","reqHeaders_latitude","reqHeaders_longitude","reqHeaders_mq-rquid",
            "reqHeaders_parent-rquid","reqHeaders_requestUID","reqHeaders_resourceOwnerID","reqHeaders_rquid","reqHeaders_scb-channel",
            "reqHeaders_source","reqHeaders_th_co_scb-easy-rquid","reqHeaders_th_co_scb-easy-sessionid","reqHeaders_tilesversion",
            "reqHeaders_user-agent","reqHeaders_userid","reqHeaders_usermode","reqHeaders_x-apn","reqHeaders_x-b3-parentspanid","reqHeaders_x-b3-sampled",
            "reqHeaders_x-b3-spanid","reqHeaders_x-b3-traceid","reqHeaders_x-forwarded-for","reqHeaders_x-forwarded-host",
            "reqHeaders_x-forwarded-port","reqHeaders_x-forwarded-prefix",
            "reqHeaders_x-forwarded-proto","reqHeaders_x-imsi","reqHeaders_x-msisdn","reqHeaders_x-oper","reqHeaders_x-rat","reqMethod",
            "reqParams","reqQueryParams","respBody","respContentLength","respHeaders","respHttpStatus","respStatus","scheme","serverName",
            "serverPort","service","servletPath","source","spanId","spanSampled","traceId","tranTime","type","uri","url"};

    String[] feapilogs = {"@timestamp","agent_ephemeral_id","agent_hostname","agent_id","agent_type","agent_version","api_correlationid",
            "api_span","api_trace","classname","ecs_version","host_name","input_type","log_file_path","log_flags","log_offset","loglevel",
            "logmessage","logtime","pid","service","tags","threadname"};

    @GetMapping(value = "/getAudit")
    public ResponseEntity<?> getAudit() {
        List<Field> entities = new ArrayList<>();
        for(int i=0;i<audits.length;i++) {
            Field field = new Field();
            field.setId((i+1));
            field.setField(audits[i]);
            entities.add(field);
        }
        return new ResponseEntity<>(entities,HttpStatus.OK);
    }
    @GetMapping(value = "/getFerequsetlog")
    public ResponseEntity<?> getFerequsetlog() {
        List<Field> entities = new ArrayList<>();
        for(int i=0;i<ferequsetlogs.length;i++) {
            Field field = new Field();
            field.setId((i+1));
            field.setField(ferequsetlogs[i]);
            entities.add(field);
        }
        return new ResponseEntity<>(entities,HttpStatus.OK);
    }
    @GetMapping(value = "/getFeApiLog")
    public ResponseEntity<?> getFeApiLog() {
        List<Field> entities = new ArrayList<>();
        for(int i=0;i<feapilogs.length;i++) {
            Field field = new Field();
            field.setId((i+1));
            field.setField(feapilogs[i]);
            entities.add(field);
        }
        return new ResponseEntity<>(entities,HttpStatus.OK);
    }



}
