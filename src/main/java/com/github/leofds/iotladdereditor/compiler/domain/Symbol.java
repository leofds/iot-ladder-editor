/*******************************************************************************
 * Copyright (C) 2021 Leonardo Fernandes
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.leofds.iotladdereditor.compiler.domain;

public class Symbol {

	private String name;
	private Kind kind;
	private Class<?> type;
	private String scope;
	
	public Symbol(String name, Kind kind, Class<?> type, String scope) {
		this.name = name;
		this.kind = kind;
		this.type = type;
		this.scope = scope;
	}
	
	public Symbol(String name, Kind kind, String scope) {
		this.name = name;
		this.kind = kind;
		this.scope = scope;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Kind getKind() {
		return kind;
	}
	
	public void setKind(Kind kind) {
		this.kind = kind;
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public void setType(Class<?> type) {
		this.type = type;
	}
	
	public String getScope() {
		return scope;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Symbol other = (Symbol) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (kind != other.kind)
			return false;
		if (type == null ){
			if(other.getType() == null){
				if(type != other.getType()){
					return false;
				}
			}else{
				return false;
			}
		}
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Symbol [name=" + name + ", kind=" + kind + ", type=" + type
				+ ", scope=" + scope + "]";
	}
}
