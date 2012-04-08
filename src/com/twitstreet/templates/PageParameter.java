/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

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
public class PageParameter {
   private String content, direct;
   public void setContent(String s) {content = s; }
   public void setDirect(String s) { direct = s; }
   public String getContent() { return content;}
   public boolean isDirect() { return Boolean.valueOf(direct).booleanValue(); }
   public PageParameter(String content, String direct) {
      this.content = content;
      this.direct = direct;
   }
}