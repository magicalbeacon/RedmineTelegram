
package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Issue {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("project")
    @Expose
    private Project project;
    @SerializedName("tracker")
    @Expose
    private Tracker tracker;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("priority")
    @Expose
    private Priority priority;
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("assigned_to")
    @Expose
    private AssignedTo assignedTo;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("due_date")
    @Expose
    private Object dueDate;
    @SerializedName("done_ratio")
    @Expose
    private int doneRatio;
    @SerializedName("is_private")
    @Expose
    private boolean isPrivate;
    @SerializedName("estimated_hours")
    @Expose
    private Object estimatedHours;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("updated_on")
    @Expose
    private String updatedOn;
    @SerializedName("closed_on")
    @Expose
    private Object closedOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public AssignedTo getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(AssignedTo assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Object getDueDate() {
        return dueDate;
    }

    public void setDueDate(Object dueDate) {
        this.dueDate = dueDate;
    }

    public int getDoneRatio() {
        return doneRatio;
    }

    public void setDoneRatio(int doneRatio) {
        this.doneRatio = doneRatio;
    }

    public boolean isIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Object getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Object estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Object getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(Object closedOn) {
        this.closedOn = closedOn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("project", project).append("tracker", tracker).append("status", status).append("priority", priority).append("author", author).append("assignedTo", assignedTo).append("category", category).append("subject", subject).append("description", description).append("startDate", startDate).append("dueDate", dueDate).append("doneRatio", doneRatio).append("isPrivate", isPrivate).append("estimatedHours", estimatedHours).append("createdOn", createdOn).append("updatedOn", updatedOn).append("closedOn", closedOn).toString();
    }

}
