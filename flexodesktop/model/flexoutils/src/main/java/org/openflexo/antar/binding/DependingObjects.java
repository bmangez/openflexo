package org.openflexo.antar.binding;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.antar.binding.AbstractBinding.BindingEvaluationContext;
import org.openflexo.antar.binding.AbstractBinding.TargetObject;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.toolbox.HasPropertyChangeSupport;

public class DependingObjects {

	private static final Logger logger = FlexoLogger.getLogger(DependingObjects.class.getName());

	public static interface HasDependencyBinding extends Observer, PropertyChangeListener {
		public List<AbstractBinding> getDependencyBindings();
	}

	private List<TargetObject> dependingObjects;
	private HasDependencyBinding observerObject;
	private boolean dependingObjectsAreComputed;

	public DependingObjects(HasDependencyBinding observerObject) {
		super();
		this.observerObject = observerObject;
		this.dependingObjects = new ArrayList<AbstractBinding.TargetObject>();
		this.dependingObjectsAreComputed = false;
	}

	public synchronized void refreshObserving(BindingEvaluationContext context) {

		List<TargetObject> updatedDependingObjects = new ArrayList<AbstractBinding.TargetObject>();
		for (AbstractBinding binding : observerObject.getDependencyBindings()) {
			List<TargetObject> targetObjects = binding.getTargetObjects(context);
			if (targetObjects != null) {
				updatedDependingObjects.addAll(targetObjects);
			}
		}
		Set<HasPropertyChangeSupport> set = new HashSet<HasPropertyChangeSupport>();
		for (TargetObject o : updatedDependingObjects) {
			if (o.target instanceof HasPropertyChangeSupport) {
				set.add((HasPropertyChangeSupport) o.target);
			}
		}
		for (HasPropertyChangeSupport hasPCSupport : set) {
			if (hasPCSupport.getDeletedProperty() != null) {
				updatedDependingObjects.add(new TargetObject(hasPCSupport, hasPCSupport.getDeletedProperty()));
			}
		}

		List<TargetObject> newDependingObjects = new ArrayList<TargetObject>();
		List<TargetObject> oldDependingObjects = new ArrayList<TargetObject>(dependingObjects);
		for (TargetObject o : updatedDependingObjects) {
			if (oldDependingObjects.contains(o)) {
				oldDependingObjects.remove(o);
			} else {
				newDependingObjects.add(o);
			}
		}
		for (TargetObject o : oldDependingObjects) {
			dependingObjects.remove(o);
			if (o.target instanceof HasPropertyChangeSupport) {
				PropertyChangeSupport pcSupport = ((HasPropertyChangeSupport) o.target).getPropertyChangeSupport();
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("Observer " + observerObject + " remove property change listener: " + o.target + " property:"
							+ o.propertyName);
				}
				pcSupport.removePropertyChangeListener(o.propertyName, observerObject);
			} else if (o.target instanceof Observable) {
				logger.fine("Widget " + observerObject + " remove observable: " + o);
				((Observable) o.target).deleteObserver(observerObject);
			}
		}
		for (TargetObject o : newDependingObjects) {
			dependingObjects.add(o);
			if (o.target instanceof HasPropertyChangeSupport) {
				PropertyChangeSupport pcSupport = ((HasPropertyChangeSupport) o.target).getPropertyChangeSupport();
				logger.fine("Observer " + observerObject + " add property change listener: " + o.target + " property:" + o.propertyName);
				pcSupport.addPropertyChangeListener(o.propertyName, observerObject);
			} else if (o.target instanceof Observable) {
				logger.fine("Observer " + observerObject + " add observable: " + o);
				((Observable) o.target).addObserver(observerObject);
			}
		}

		dependingObjectsAreComputed = true;
	}

	public synchronized void stopObserving() {
		for (TargetObject o : dependingObjects) {
			if (o.target instanceof HasPropertyChangeSupport) {
				PropertyChangeSupport pcSupport = ((HasPropertyChangeSupport) o.target).getPropertyChangeSupport();
				// logger.info("Widget "+getWidget()+" remove property change listener: "+o.target+" property:"+o.propertyName);
				pcSupport.removePropertyChangeListener(o.propertyName, observerObject);
			} else if (o.target instanceof Observable) {
				// logger.info("Widget "+getWidget()+" remove observable: "+o);
				((Observable) o.target).deleteObserver(observerObject);
			}
		}
		dependingObjects.clear();
		dependingObjectsAreComputed = false;
	}

	public boolean areDependingObjectsComputed() {
		return dependingObjectsAreComputed;
	}

}
