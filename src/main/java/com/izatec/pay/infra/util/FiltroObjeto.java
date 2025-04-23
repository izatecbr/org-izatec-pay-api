package com.izatec.pay.infra.util;

import java.util.HashMap;

//https://www.baeldung.com/spring-request-param
//https://stackoverflow.com/questions/56468760/how-to-collect-all-fields-annotated-with-requestparam-into-one-object
//https://stackoverflow.com/questions/59420202/send-a-map-as-request-param-to-a-get-request-in-spring-boot-app

//{{IZGC_HOST_URL}}/pub/filtro?filtros=[id]=123&filtros[nome]=jose maria&filtros[enderecoNumero]=SN
public class FiltroObjeto<K,V> extends HashMap<String,String> {

}
