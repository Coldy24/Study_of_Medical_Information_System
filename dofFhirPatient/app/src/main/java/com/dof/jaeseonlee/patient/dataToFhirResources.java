package com.dof.jaeseonlee.patient;

import android.os.AsyncTask;

import java.util.Date;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Device;
import ca.uhn.fhir.model.dstu2.resource.DeviceComponent;
import ca.uhn.fhir.model.dstu2.resource.DeviceMetric;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;

/**
 * Created by 이재선 on 2018-11-26.
 */

public class dataToFhirResources{
    String serverAddress;
    //Patient patient = new
    Bundle bundle = new Bundle();
    Device device = new Device();
    DeviceComponent deviceComponent1 = new DeviceComponent();
    DeviceMetric deviceMetric = new DeviceMetric();
    Observation observation = new Observation();
    Patient patient = new Patient();





    /*
    TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
PhoneNum = telManager.getLine1Number();
if(PhoneNum.startsWith("+82")){
    PhoneNum = PhoneNum.replace("+82", "0");
}




     */





    /*public dataToFhirResources(String fam,String giv,String phone,boolean isMan,Date birth, int sys, int dia, int mean,String svrAddress){
        serverAddress = svrAddress;
        makeBundle();
        makePatient(fam, giv, phone, isMan, birth);
        makeDevice();
        makeDeviceComponent();
        //makeDeviceMetric();
        makeObservation(sys, dia, mean);
    }*/

    /*
    public void makeBundle(){
        bundle.setType(BundleTypeEnum.TRANSACTION);
    }

    public void makePatient(String fam, String giv, String phone, boolean isMan, Date birth){
        patient.addIdentifier()
                .setSystem("http:www.knu.ac.kr")
                .setValue("KNU003");
        patient.setActive(true);

        patient.addName()
                .setUse(NameUse.USUAL)
                .setFamily(fam)
                .addGiven(giv);

        patient.addTelecom()
                .setSystem(ContactPointSystem.PHONE)
                .setValue(phone)
                .setUse(ContactPointUse.HOME);

        patient.setGender(isMan?AdministrativeGender.MALE : AdministrativeGender.FEMALE);
        patient.setBirthDate(birth);

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(patient)
                .getRequest()
                .setUrl("Patient")
                .setMethod(HTTPVerb.POST);
    }

    public void makeDevice(){
        device.addIdentifier()
                .setSystem("http:www.vanila.co.kr")
                .setValue("device001");

        device.setStatus(FHIRDeviceStatus.ACTIVE);
        device.setId("1");
        CodeableConcept codeableConcept = new CodeableConcept();
        codeableConcept.addCoding().setSystem("urn:iso:std:iso:11073:10101")
                .setCode("4172")
                .setDisplay("MDC_DEV_ANALY_PRESS_BLD");
        device.setType(codeableConcept);
        device.setLotNumber("20150301001");
        device.setManufacturer("VANILLA");
        device.setManufactureDate(new Date(1985,1,1));
        device.setModel("BPM01");
        device.setPatient(new Reference("p1"));

        //.setFullUrl("Device/" + UUID.randomUUID().toString())

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(device)
                .getRequest()
                .setUrl("device")
                .setMethod(HTTPVerb.POST);
    }

    public void makeDeviceComponent(){
        Identifier identifier = new Identifier();
        identifier.setSystem("http:www.vanila.co.kr")
                .setValue("mds001");
        deviceComponent1.setIdentifier(identifier);
        deviceComponent1.setId("DeviceComponent/1");
        CodeableConcept codeableConcept = new CodeableConcept();
        codeableConcept.addCoding().setSystem("urn:iso:std:iso:11073:10101")
                .setCode("4173")
                .setDisplay("MDC_DEV_ANALY_PRESS_BLD_MDS");
        deviceComponent1.setType(codeableConcept);
        deviceComponent1.setLastSystemChange(new Date(1985,1,1));
        Reference reference = new Reference(bundle.getEntry().get(1).getFullUrl());
        deviceComponent1.setSource(reference);

        ArrayList<CodeableConcept> coding = new ArrayList<CodeableConcept>();
        CodeableConcept code = new CodeableConcept();
        code.addCoding().setCode("on");
        deviceComponent1.setOperationalStatus(coding);

        //		.setFullUrl("urn:uuid:" + UUID.randomUUID().toString())

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(deviceComponent1)
                .getRequest()
                .setUrl("DeviceComponent")
                .setMethod(HTTPVerb.POST);
    }

    public void makeDeviceMetric(){
        Identifier identifier = new Identifier();
        identifier.setSystem("http://www.vanilla.kr")
                .setValue("metric001");
        deviceMetric.setIdentifier(identifier);

        CodeableConcept typeCodeableConcept = new CodeableConcept();
        typeCodeableConcept.addCoding().setSystem("https://rtmms.nist.gov")
                .setCode("150020")
                .setDisplay("MDC_PRESS_BLD_NONINV");
        deviceMetric.setType(typeCodeableConcept);

        CodeableConcept unitCodeableConcept = new CodeableConcept();
        unitCodeableConcept.addCoding()
                .setSystem("https://rtmms.nist.gov")
                .setCode("266016")
                .setDisplay("MDC_DIM_MMHG");
        deviceMetric.setUnit(unitCodeableConcept);

        //1은 Device 4는 CHAN DeviceComponent
        deviceMetric.setSource(new Reference(bundle.getEntry().get(1).getFullUrl()));
        deviceMetric.setParent(new Reference(bundle.getEntry().get(4).getFullUrl()));

        deviceMetric.setOperationalStatus(DeviceMetricOperationalStatus.ON);
        deviceMetric.setCategory(DeviceMetricCategory.MEASUREMENT);

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(deviceMetric)
                .getRequest()
                .setUrl("DeviceMetric")
                .setMethod(HTTPVerb.POST);
    }

    public void makeObservation(int sys, int dia, int mean){
        Identifier identifier = new Identifier();
        identifier.setSystem("http://www.knu.ac.kr")
                .setValue("bpm001");
        observation.addIdentifier(identifier);
        observation.setStatus(ObservationStatus.REGISTERED);

        CodeableConcept CodeableConcept = new CodeableConcept();
        CodeableConcept.addCoding().setSystem("https://rtmms.nist.gov")
                .setCode("150020")
                .setDisplay("MDC_PRESS_BLD_NONINV");
        observation.setCode(CodeableConcept);

        //0은 Patient
        observation.setSubject(new Reference(bundle.getEntry().get(0).getFullUrl()));

        CodeableConcept bodyCode = new CodeableConcept();
        bodyCode.addCoding()
                .setSystem("http://snomed.info/sct")
                .setCode("368209003");
        observation.setBodySite(bodyCode);

        observation.setDevice(new Reference(bundle.getEntry().get(1).getFullUrl()));

        Quantity sysValue = new Quantity();
        sysValue.setValue(sys).
                setUnit("mm[Hg]");
        CodeableConcept compoCode1 = new CodeableConcept();
        compoCode1.addCoding()
                .setSystem("https://rtmms.nist.gov")
                .setCode("150021")
                .setDisplay("MDC_PRESS_BLD_NONINV_SYS");
        ObservationComponentComponent compo1 = observation.addComponent();
        compo1.setCode(compoCode1)
                .setValue(sysValue);

        Quantity diaValue = new Quantity();
        diaValue.setValue(dia).
                setUnit("mm[Hg]");
        CodeableConcept compoCode2 = new CodeableConcept();
        compoCode2.addCoding()
                .setSystem("https://rtmms.nist.gov")
                .setCode("150022")
                .setDisplay("MDC_PRESS_BLD_NONINV_DIA");
        ObservationComponentComponent compo2 = observation.addComponent();
        compo2.setCode(compoCode2)
                .setValue(diaValue);

        Quantity meanValue = new Quantity();
        meanValue.setValue(mean).
                setUnit("mm[Hg]");
        CodeableConcept compoCode3 = new CodeableConcept();
        compoCode3.addCoding()
                .setSystem("https://rtmms.nist.gov")
                .setCode("150023")
                .setDisplay("MDC_PRESS_BLD_NONINV_MEAN");
        ObservationComponentComponent compo3 = observation.addComponent();
        compo3.setCode(compoCode3)
                .setValue(meanValue);

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(observation)
                .getRequest()
                .setUrl("Observation")
                .setMethod(HTTPVerb.POST);
    }

    //getter and setter
    public Bundle getBundle() {
        return bundle;
    }
    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
    public Device getDevice() {
        return device;
    }
    public void setDevice(Device device) {
        this.device = device;
    }
    public DeviceComponent getComponent1() {
        return deviceComponent1;
    }
    public void setComponent1(DeviceComponent component1) {
        this.deviceComponent1 = component1;
    }
    public DeviceComponent getComponent2() {
        return deviceComponent2;
    }
    public void setComponent2(DeviceComponent component2) {
        this.deviceComponent2 = component2;
    }
    public DeviceComponent getComponent3() {
        return deviceComponent3;
    }
    public void setComponent3(DeviceComponent component3) {
        this.deviceComponent3 = component3;
    }
    public DeviceMetric getDeviceMetric() {
        return deviceMetric;
    }
    public void setDeviceMetric(DeviceMetric deviceMetric) {
        this.deviceMetric = deviceMetric;
    }
    public Observation getObservation() {
        return observation;
    }
    public void setObservation(Observation observation) {
        this.observation = observation;
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void POSTMessage(){
        // Log the request
        FhirContext ctx = FhirContext.forDstu3();
        String bundleString =ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);
        System.out.println(bundleString);

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(serverAddress);
        System.out.println("Send Resource to server: " + serverAddress);

        httppost.setHeader("Content-Type", "application/fhir+xml");
        httppost.setEntity(new ByteArrayEntity(bundleString.getBytes()));

        //Execute and get the response.
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                if (entity != null) {
                    // EntityUtils to get the response content
                    String content =  EntityUtils.toString(entity);
                    System.out.println("responds:"+content);
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
