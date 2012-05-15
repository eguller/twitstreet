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
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class GetTag extends TagSupport {
   private String name;
   // setter method for name attribute
   public void setName(String name) {
      this.name = name;
   }
   public int doStartTag() throws JspException {
      // obtain reference to template stack
      Stack stack = (Stack)pageContext.getAttribute(
               "template-stack", PageContext.REQUEST_SCOPE);
      // stack should not be null
      if(stack == null)
         throw new JspException("GetTag.doStartTag(): " +
                                 "NO STACK");
      // peek at hashtable
      Hashtable params = (Hashtable)stack.peek();
      // hashtable should not be null
      if(params == null)
         throw new JspException("GetTag.doStartTag(): " +
                                 "NO HASHTABLE");
      // get page parameter from hashtable
      PageParameter param = (PageParameter)params.get(name);
      if(param != null) {
         String content = param.getContent();
         if(param.isDirect()) {
            // print content if direct attribute is true
            try {
               pageContext.getOut().print(content);
            }
            catch(java.io.IOException ex) {
               throw new JspException(ex.getMessage());
            }
         }
         else {
            // include content if direct attribute is false
            try {
               pageContext.getOut().flush();
               pageContext.include(content);
            }
            catch(Exception ex) {
               throw new JspException(ex.getMessage());
            }
         }
      }
      return SKIP_BODY; // not interested in tag body, if present
   }
   // tag handlers should always implement release() because
   // handlers can be reused by the JSP container
   public void release() {
      name = null;
   }
}