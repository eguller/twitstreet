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
public class InsertTag extends TagSupport {
   private String template;
   private Stack stack;
   // setter method for template attribute
   public void setTemplate(String template) {
      this.template = template;
   }
   public int doStartTag() throws JspException {
      stack = getStack(); // obtain a reference to the template stack
      stack.push(new Hashtable()); // push new hashtable onto stack
      return EVAL_BODY_INCLUDE;  // pass tag body through unchanged
   }
   public int doEndTag() throws JspException {
      try {
         pageContext.include(template); // include template
      }
      catch(Exception ex) { // IOException or ServletException
         throw new JspException(ex.getMessage()); // recast exception
      }
      stack.pop(); // pop hashtable off stack
      return EVAL_PAGE; // evaluate the rest of the page after the tag
   }
   // tag handlers should always implement release() because
   // handlers can be reused by the JSP container
   public void release() {
      template = null;
      stack = null;
   }
   public Stack getStack() {
      // try to get stack from request scope
      Stack s = (Stack)pageContext.getAttribute(
                        "template-stack",
                        PageContext.REQUEST_SCOPE);
      // if the stack's not present, create a new one and
      // put it into request scope
      if(s == null) {
         s = new Stack();
         pageContext.setAttribute("template-stack", s,
                        PageContext.REQUEST_SCOPE);
      }
      return s;
   }
}