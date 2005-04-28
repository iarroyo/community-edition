/*
 * Created on Mar 15, 2005
 */
package com.activiti.web.jsf;

import java.util.Map;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import com.activiti.web.data.IDataContainer;

import com.sun.faces.renderkit.html_basic.FormRenderer;

/**
 * @author kevinr
 */
public final class Utils
{
   /**
    * Encodes the given string, so that it can be used within an HTML page.
    * 
    * @param string     the String to convert
    */
   public static String encode(String string)
   {
      if (string == null)
      {
         return "";
      }

      StringBuilder sb = null;      //create on demand
      String enc;
      char c;
      for (int i = 0; i < string.length(); i++)
      {
         enc = null;
         c = string.charAt(i);
         switch (c)
         {
            case '"': enc = "&quot;"; break;    //"
            case '&': enc = "&amp;"; break;     //&
            case '<': enc = "&lt;"; break;      //<
            case '>': enc = "&gt;"; break;      //>
             
            //german umlauts
            case '\u00E4' : enc = "&auml;";  break;
            case '\u00C4' : enc = "&Auml;";  break;
            case '\u00F6' : enc = "&ouml;";  break;
            case '\u00D6' : enc = "&Ouml;";  break;
            case '\u00FC' : enc = "&uuml;";  break;
            case '\u00DC' : enc = "&Uuml;";  break;
            case '\u00DF' : enc = "&szlig;"; break;
            
            //misc
            //case 0x80: enc = "&euro;"; break;  sometimes euro symbol is ascii 128, should we suport it?
            case '\u20AC': enc = "&euro;";  break;
            case '\u00AB': enc = "&laquo;"; break;
            case '\u00BB': enc = "&raquo;"; break;
            case '\u00A0': enc = "&nbsp;"; break;
            
            default:
               if (((int)c) >= 0x80)
               {
                  //encode all non basic latin characters
                  enc = "&#" + ((int)c) + ";";
               }
               break;
         }
         
         if (enc != null)
         {
            if (sb == null)
            {
               String soFar = string.substring(0, i);
               sb = new StringBuilder(i + 8);
               sb.append(soFar);
            }
            sb.append(enc);
         }
         else
         {
            if (sb != null)
            {
               sb.append(c);
            }
         }
      }
      
      if (sb == null)
      {
         return string;
      }
      else
      {
         return sb.toString();
      }
   }
   
   /**
    * Generate the JavaScript to submit set the specified hidden Form field to the
    * supplied value and submit the parent Form.
    * 
    * NOTE: the supplied hidden field name is added to the Form Renderer map for output.
    * 
    * @param context       FacesContext
    * @param component     UIComponent to generate JavaScript for
    * @param fieldId       Hidden field id to set value for
    * @param fieldValue    Hidden field value to set hidden field too on submit
    * 
    * @return JavaScript event code
    */
   public static String generateFormSubmit(FacesContext context, UIComponent component, String fieldId, String fieldValue)
   {
      return generateFormSubmit(context, component, fieldId, fieldValue, null);
   }
   
   /**
    * Generate the JavaScript to submit set the specified hidden Form field to the
    * supplied value and submit the parent Form.
    * 
    * NOTE: the supplied hidden field name is added to the Form Renderer map for output.
    * 
    * @param context       FacesContext
    * @param component     UIComponent to generate JavaScript for
    * @param fieldId       Hidden field id to set value for
    * @param fieldValue    Hidden field value to set hidden field too on submit
    * @param params        Optional map of param name/values to output
    * 
    * @return JavaScript event code
    */
   public static String generateFormSubmit(FacesContext context, UIComponent component, String fieldId, String fieldValue, Map<String, String> params)
   {
      UIForm form = Utils.getParentForm(context, component);
      if (form == null)
      {
         throw new IllegalStateException("Must nest components inside UIForm to generate form submit!");
      }
      
      String formClientId = form.getClientId(context);
      
      StringBuilder buf = new StringBuilder(200);
      buf.append("document.forms[");
      buf.append("'");
      buf.append(formClientId);
      buf.append("'");
      buf.append("]['");
      buf.append(fieldId);
      buf.append("'].value='");
      buf.append(fieldValue);
      buf.append("';");
      
      if (params != null)
      {
         for (String name : params.keySet())
         {
            buf.append("document.forms[");
            buf.append("'");
            buf.append(formClientId);
            buf.append("'");
            buf.append("]['");
            buf.append(name);
            buf.append("'].value='");
            buf.append(params.get(name));
            buf.append("';");
            
            // weak, but this seems to be the way Sun RI do it...
            FormRenderer.addNeededHiddenField(context, name);
         }
      }
      
      buf.append("document.forms[");
      buf.append("'");
      buf.append(formClientId);
      buf.append("'");
      buf.append("].submit()");
      
      buf.append(";return false;");
      
      // weak, but this seems to be the way Sun RI do it...
      FormRenderer.addNeededHiddenField(context, fieldId);
      
      return buf.toString();
   }
   
   /**
    * Build a context path safe image tag for the supplied image path.
    * Image path should be supplied with a leading slash '/'.
    * 
    * @param context       FacesContext
    * @param image         The local image path from the web folder with leading slash '/'
    * @param width         Width in pixels
    * @param height        Height in pixels
    * @param alt           Optional alt/title text
    * 
    * @return Populated <code>img</code> tag
    */
   public static String buildImageTag(FacesContext context, String image, int width, int height, String alt)
   {
      StringBuilder buf = new StringBuilder(128);
      
      buf.append("<img src='")
         .append(context.getExternalContext().getRequestContextPath())
         .append(image)
         .append("' width=")
         .append(width)
         .append(" height=")
         .append(height)
         .append(" border=0");
      
      if (alt != null)
      {
         alt = Utils.encode(alt);
         buf.append(" alt=\"")
            .append(alt)
            .append("\" title=\"")
            .append(alt)
            .append('"');
      }
      
      buf.append('>');
      
      return buf.toString();
   }
   
   /**
    * Build a context path safe image tag for the supplied image path.
    * Image path should be supplied with a leading slash '/'.
    * 
    * @param context       FacesContext
    * @param image         The local image path from the web folder with leading slash '/'
    * @param alt           Optional alt/title text
    * 
    * @return Populated <code>img</code> tag
    */
   public static String buildImageTag(FacesContext context, String image, String alt)
   {
      StringBuilder buf = new StringBuilder(128);
      
      buf.append("<img src='")
         .append(context.getExternalContext().getRequestContextPath())
         .append(image)
         .append("' border=0");
      
      if (alt != null)
      {
         alt = Utils.encode(alt);
         buf.append(" alt=\"")
            .append(alt)
            .append("\" title=\"")
            .append(alt)
            .append('"');
      }
      
      buf.append('>');
      
      return buf.toString();
   }
   
   /**
    * Return the parent UIForm component for the specified UIComponent
    * 
    * @param context       FaceContext
    * @param component     The UIComponent to find parent Form for
    * 
    * @return UIForm parent or null if none found in hiearachy
    */
   public static UIForm getParentForm(FacesContext context, UIComponent component)
   {
      UIComponent parent = component.getParent();
      while (parent != null)
      {
         if (parent instanceof UIForm)
         {
            break;
         }
         parent = parent.getParent();
      }
      return (UIForm)parent;
   }
   
   /**
    * Return the parent UIComponent implementing the NamingContainer interface for
    * the specified UIComponent.
    * 
    * @param context       FaceContext
    * @param component     The UIComponent to find parent Form for
    * 
    * @return NamingContainer parent or null if none found in hiearachy
    */
   public static UIComponent getParentNamingContainer(FacesContext context, UIComponent component)
   {
      UIComponent parent = component.getParent();
      while (parent != null)
      {
         if (parent instanceof NamingContainer)
         {
            break;
         }
         parent = parent.getParent();
      }
      return (UIComponent)parent;
   }
   
   /**
    * Return the parent UIComponent implementing the IDataContainer interface for
    * the specified UIComponent.
    * 
    * @param context       FaceContext
    * @param component     The UIComponent to find parent IDataContainer for
    * 
    * @return IDataContainer parent or null if none found in hiearachy
    */
   public static IDataContainer getParentDataContainer(FacesContext context, UIComponent component)
   {
      UIComponent parent = component.getParent();
      while (parent != null)
      {
         if (parent instanceof IDataContainer)
         {
            break;
         }
         parent = parent.getParent();
      }
      return (IDataContainer)parent;
   }
}
