package com.activiti.repo.dictionary.metamodel;

import com.activiti.repo.dictionary.PropertyDefinition;

/**
 * Property Definition
 * 
 * @author David Caruana
 */
public interface M2Property
{
    public M2Class getContainerClass();

    public String getName();
    
    public void setName(String name);
    
    public M2PropertyType getType();
    
    public void setType(M2PropertyType type);
    
    public boolean isProtected();
    
    public void setProtected(boolean isProtected);
    
    public boolean isMandatory();
    
    public void setMandatory(boolean isMandatory);
    
    public boolean isMultiValued();
    
    public void setMultiValued(boolean isMultiValued);
    
    public boolean isIndexed();
    
    public void setIndexed(boolean isIndexed);
    
    public boolean isStoredInIndex();
    
    public void setStoredInIndex(boolean isStoredInIndex);
    
  

    // TODO: public List getDefaultValues();
    
    // TODO:  public List/*M2ValueConstraint*/ getValueConstraints();
    
    /**
     * @return Returns a read-only property definition
     */
    public PropertyDefinition getPropertyDefinition();

    public boolean isIndexedAtomically();
    
    public boolean setIndexedAtomically(boolean isIndexedAtomically);

    public boolean isTokenisedInIndex();
    
    public boolean setTokenisedInIndex(boolean isTokenisedInIndex);
    
    
}
