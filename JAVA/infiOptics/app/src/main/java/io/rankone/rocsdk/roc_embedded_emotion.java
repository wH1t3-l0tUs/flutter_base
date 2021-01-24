/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.rankone.rocsdk;

public class roc_embedded_emotion {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  public roc_embedded_emotion(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(roc_embedded_emotion obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        rocJNI.delete_roc_embedded_emotion(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setAnger(float value) {
    rocJNI.roc_embedded_emotion_anger_set(swigCPtr, this, value);
  }

  public float getAnger() {
    return rocJNI.roc_embedded_emotion_anger_get(swigCPtr, this);
  }

  public void setDisgust(float value) {
    rocJNI.roc_embedded_emotion_disgust_set(swigCPtr, this, value);
  }

  public float getDisgust() {
    return rocJNI.roc_embedded_emotion_disgust_get(swigCPtr, this);
  }

  public void setFear(float value) {
    rocJNI.roc_embedded_emotion_fear_set(swigCPtr, this, value);
  }

  public float getFear() {
    return rocJNI.roc_embedded_emotion_fear_get(swigCPtr, this);
  }

  public void setJoy(float value) {
    rocJNI.roc_embedded_emotion_joy_set(swigCPtr, this, value);
  }

  public float getJoy() {
    return rocJNI.roc_embedded_emotion_joy_get(swigCPtr, this);
  }

  public void setNeutral(float value) {
    rocJNI.roc_embedded_emotion_neutral_set(swigCPtr, this, value);
  }

  public float getNeutral() {
    return rocJNI.roc_embedded_emotion_neutral_get(swigCPtr, this);
  }

  public void setSadness(float value) {
    rocJNI.roc_embedded_emotion_sadness_set(swigCPtr, this, value);
  }

  public float getSadness() {
    return rocJNI.roc_embedded_emotion_sadness_get(swigCPtr, this);
  }

  public void setSurprise(float value) {
    rocJNI.roc_embedded_emotion_surprise_set(swigCPtr, this, value);
  }

  public float getSurprise() {
    return rocJNI.roc_embedded_emotion_surprise_get(swigCPtr, this);
  }

  public roc_embedded_emotion() {
    this(rocJNI.new_roc_embedded_emotion(), true);
  }

}
