package com.exadel.coolDesking.workspace;

import com.exadel.coolDesking.floorPlan.FloorPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkplaceFilter implements Specification<Workplace> {
    private Integer workplaceNumber;
    private WorkplaceType type;
    private Boolean isNextToWindow;
    private Boolean hasPc;
    private Boolean hasMonitor;
    private Boolean hasKeyboard;
    private Boolean hasMouse;
    private Boolean hasHeadSet;
    private Integer floor;
    private Boolean kitchen;
    private Boolean confRoom;
    private UUID officeId;

    @Override
    public Predicate toPredicate(Root<Workplace> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Join<Workplace, FloorPlan> mapRoot = root.join("floorPlan");
        ArrayList<Predicate> predicates = new ArrayList<>();

        if (officeId != null){
            predicates.add(cb.equal(mapRoot.get("office").get("id"), officeId));
        }

        if (workplaceNumber != null) {
            predicates.add(cb.equal(root.get("workplaceNumber"), workplaceNumber));
        }

        if (type != null) {
            predicates.add(cb.equal(root.get("type"), type));
        }

        if (isNextToWindow != null) {
            predicates.add(cb.equal(root.get("isNextToWindow"), isNextToWindow));
        }

        if (hasPc != null) {
            predicates.add(cb.equal(root.get("hasPc"), hasPc));
        }

        if (hasMonitor != null) {
            predicates.add(cb.equal(root.get("hasMonitor"), hasMonitor));
        }

        if (hasKeyboard != null) {
            predicates.add(cb.equal(root.get("hasKeyboard"), hasKeyboard));
        }

        if (hasHeadSet != null) {
            predicates.add(cb.equal(root.get("hasHeadSet"), hasHeadSet));
        }

        if (hasMouse != null) {
            predicates.add(cb.equal(root.get("hasMouse"), hasMouse));
        }

        if (floor != null) {
            predicates.add(cb.equal(root.get("floor"), floor));
        }

        if (kitchen != null) {
            predicates.add(cb.equal(root.get("kitchen"), kitchen));
        }

        if (confRoom != null) {
            predicates.add(cb.equal(root.get("confRoom"), confRoom));
        }

        return predicates.size() <= 0 ? null : cb.and(predicates.toArray(new Predicate[0]));
    }
}
