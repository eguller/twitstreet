/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanthe@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package com.twitstreet.templates;
import java.util.Hashtable;
import java.util.Stack;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class PutTag extends TagSupport {
   private String name, content, direct="false";
   // setter methods for Put tag attributes
   public void setName(String s) { name = s; }
   public void setContent(String s) {content = s; }
   public void setDirect(String s) { direct = s; }
   public int doStartTag() throws JspException {
      // obtain a reference to enclosing insert tag
      InsertTag parent = (InsertTag)getAncestor(
                              "com.twitstreet.templates.InsertTag");
      // put tags must be enclosed in an insert tag
      if(parent == null)
         throw new JspException("PutTag.doStartTag(): " +
                                "No InsertTag ancestor");
      // get template stack from insert tag
      Stack template_stack = parent.getStack();
      // template stack should never be null
      if(template_stack == null)
         throw new JspException("PutTag: no template stack");
      // peek at hashtable on the stack
      Hashtable params = (Hashtable)template_stack.peek();
      // hashtable should never be null either
      if(params == null)
         throw new JspException("PutTag: no hashtable");
      // put a new PageParameter in the hashtable
      params.put(name, new PageParameter(content, direct));
      return SKIP_BODY; // not interested in tag body, if present
   }
   // tag handlers should always implement release() because
   // handlers can be reused by the JSP container
   public void release() {
      name = content = direct = null;
   }
   // convenience method for finding ancestor names with
   // a specific class name
   private TagSupport getAncestor(String className)
                                 throws JspException {
      Class klass = null; // can't name variable "class"
      try {
         klass = Class.forName(className);
      }
      catch(ClassNotFoundException ex) {
         throw new JspException(ex.getMessage());
      }
      return (TagSupport)findAncestorWithClass(this, klass);
   }
}