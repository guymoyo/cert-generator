package de.adorsys.tppserver;

public class Server {

	private String name;
	private String description;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Server [name=" + name + ", description=" + description + "]";
	}
	
	
}
