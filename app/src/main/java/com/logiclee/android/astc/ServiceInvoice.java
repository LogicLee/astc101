package com.logiclee.android.astc;

/**
 * This class represents a restaurant or service invoice, bill, or check.
 * It calculates the tip percentage, tip amount, and bill total based on 
 * service quality, on a scale from 1 to 5. 
 * @author Nigel C. Lee, March 2013
 */
public class ServiceInvoice {
	enum ServiceQualityEnum { ONESTAR, TWOSTAR, THREESTAR, FOURSTAR, FIVESTAR }
	ServiceQualityEnum serviceQuality;
	float billAmount;
	float tipPercentage;
	float billTotal;
	
	public ServiceInvoice() {
		billAmount = 0.0F;		
		setServiceQuality(ServiceQualityEnum.THREESTAR);
		billTotal = 0.0F;
	}

//  Bill amount.......................................................	
	public void setBillAmount(float amount) {
		this.billAmount = amount;
		calculate();
	}

//  Tip amount........................................................	
	public float getTipAmount() {
		return this.billAmount * this.tipPercentage;
	}
	public float getTipPercentage() {
		return this.tipPercentage;
	}
	
//	Service Quality...................................................	
	public ServiceQualityEnum getServiceQuality() {
		return serviceQuality;
	}
	public void setServiceQuality(ServiceQualityEnum sq) {
		this.serviceQuality = sq;
		switch (this.serviceQuality) {
		
		case ONESTAR: 
			this.tipPercentage = 0.05F;
			break;
			
		case TWOSTAR: 
			this.tipPercentage = 0.10F;
			break;

		case FOURSTAR: 
			this.tipPercentage = 0.20F;
			break;
			
		case FIVESTAR: 
			this.tipPercentage = 0.25F;	
			break;
			
		default: 
			this.tipPercentage = 0.15F;
		}
		calculate();
	}

//	Bill Total........................................................	
	public void calculate() {
		this.billTotal = this.billAmount + getTipAmount();
	}
	
	public float getBillTotal() {
		calculate();
		return this.billTotal;
	}
	
}
