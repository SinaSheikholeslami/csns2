/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012, Chengyu Sun (csun@calstatela.edu).
 * 
 * CSNS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * CSNS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CSNS. If not, see http://www.gnu.org/licenses/agpl.html.
 */
package csns.model.academics.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csns.model.academics.Assignment;
import csns.model.academics.OnlineAssignment;
import csns.model.academics.Section;
import csns.model.academics.dao.AssignmentDao;
import csns.model.core.User;

@Repository
public class AssignmentDaoImpl implements AssignmentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Assignment getAssignment( Long id )
    {
        return entityManager.find( Assignment.class, id );
    }

    @Override
    public List<OnlineAssignment> getOnlineAssignments( Section section )
    {
        String query = "from OnlineAssignment where section = :section "
            + "order by name asc";

        return entityManager.createQuery( query, OnlineAssignment.class )
            .setParameter( "section", section )
            .getResultList();
    }

    @Override
    public List<OnlineAssignment> getOnlineAssignments( User instructor )
    {
        String query = "select assignment from OnlineAssignment assignment "
            + "join assignment.section.instructors instructor "
            + "where instructor = :instructor order by assignment.dueDate desc";

        return entityManager.createQuery( query, OnlineAssignment.class )
            .setParameter( "instructor", instructor )
            .getResultList();
    }

    @Override
    @Transactional
    public Assignment saveAssignment( Assignment assignment )
    {
        return entityManager.merge( assignment );
    }

}