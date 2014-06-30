/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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
package csns.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import csns.model.assessment.RubricEvaluation;
import csns.model.assessment.dao.RubricEvaluationDao;
import csns.security.SecurityUtils;

@Controller
public class RubricEvaluationController {

    @Autowired
    private RubricEvaluationDao rubricEvaluationDao;

    private static final Logger logger = LoggerFactory.getLogger( RubricEvaluationController.class );

    @RequestMapping("/rubric/evaluation/{role}/view")
    public String view( @PathVariable String role, @RequestParam Long id,
        ModelMap models )
    {
        models.put( "role", role );
        models.put( "evaluation", rubricEvaluationDao.getRubricEvaluation( id ) );
        return "rubric/evaluation/view";
    }

    @RequestMapping("/rubric/evaluation/{role}/set")
    @ResponseBody
    public ResponseEntity<String> set( @RequestParam Long id,
        @RequestParam int index, @RequestParam int value )
    {
        RubricEvaluation evaluation = rubricEvaluationDao.getRubricEvaluation( id );
        // Ignore the request if the rubric assignment is already past due.
        if( evaluation.getSubmission().getAssignment().isPastDue() )
            return new ResponseEntity<String>( HttpStatus.BAD_REQUEST );

        evaluation.getRatings().set( index, value + 1 );
        rubricEvaluationDao.saveRubricEvaluation( evaluation );

        logger.info( SecurityUtils.getUser().getUsername() + " rated "
            + (value + 1) + " for indicator " + index
            + " in rubric evaluation " + id );

        return new ResponseEntity<String>( HttpStatus.OK );
    }

}