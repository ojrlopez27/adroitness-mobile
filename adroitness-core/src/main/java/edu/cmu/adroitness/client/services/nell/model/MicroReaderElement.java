package edu.cmu.adroitness.client.services.nell.model;

/**
 * Created by oscarr on 9/26/16.
 */
public class MicroReaderElement {
    private int spanStart;
    private int endStart;
    private double confidence;
    private String documentId;
    private String slot;
    private String annotator;
    private String annotationTime;
    private MicroReaderSlotValue value;

    public int getSpanStart() {
        return spanStart;
    }

    public void setSpanStart(int spanStart) {
        this.spanStart = spanStart;
    }

    public int getEndStart() {
        return endStart;
    }

    public void setEndStart(int endStart) {
        this.endStart = endStart;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getAnnotator() {
        return annotator;
    }

    public void setAnnotator(String annotator) {
        this.annotator = annotator;
    }

    public String getAnnotationTime() {
        return annotationTime;
    }

    public void setAnnotationTime(String annotationTime) {
        this.annotationTime = annotationTime;
    }

    public MicroReaderSlotValue getValue() {
        return value;
    }

    public void setValue(MicroReaderSlotValue value) {
        this.value = value;
    }
}
