package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterAuthenticateDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterCreateDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterUpdateDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterUpdatePasswordDTO;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.facade.MushroomHunterFacade;
import cz.muni.fi.pa165.mushrooms.service.BeanMappingService;
import cz.muni.fi.pa165.mushrooms.service.MushroomHunterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of {@link MushroomHunterFacade} interface.
 *
 * @author bkompis
 */
@Transactional
@Service
public class MushroomHunterFacadeImpl implements MushroomHunterFacade {
    @Inject
    private MushroomHunterService service;

    @Autowired
    private BeanMappingService beanMappingService;


    @Override
    public MushroomHunterDTO findHunterById(Long hunterId) {
        MushroomHunter hunter = service.findHunterById(hunterId);
        return (hunter == null) ? null : beanMappingService.mapTo(hunter, MushroomHunterDTO.class);
    }

    @Override
    public MushroomHunterDTO findHunterByNickname(String nickname) {
        MushroomHunter hunter = service.findHunterByNickname(nickname);
        return (hunter == null) ? null : beanMappingService.mapTo(hunter, MushroomHunterDTO.class);
    }

    @Override
    public MushroomHunterDTO registerHunter(MushroomHunterCreateDTO hunter) {
        //manually create a new object to pass to service
        MushroomHunter newEntity = new MushroomHunter();
        newEntity.setUserNickname(hunter.getUserNickname());
        newEntity.setFirstName(hunter.getFirstName());
        newEntity.setSurname(hunter.getSurname());
        newEntity.setPersonalInfo(hunter.getPersonalInfo());
        newEntity.setAdmin(hunter.isAdmin());

        service.registerHunter(newEntity, hunter.getUnencryptedPassword());

        return findHunterByNickname(hunter.getUserNickname());
    }

    @Override
    public boolean deleteHunter(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Null id at hunter delete."); //TODO: ex throwing lower; DataAccessException
        }
        MushroomHunter hunter = beanMappingService.mapTo(findHunterById(id), MushroomHunter.class);
        service.deleteHunter(hunter);
        return true;
    }

    @Override
    public MushroomHunterDTO updateHunter(MushroomHunterUpdateDTO hunter) {
        //TODO: check Dozer behaviour
        MushroomHunter entity = service.findHunterByNickname(hunter.getUserNickname());
        entity.setSurname(hunter.getSurname());
        entity.setFirstName(hunter.getFirstName());
        entity.setUserNickname(hunter.getUserNickname());
        entity.setPersonalInfo(hunter.getPersonalInfo());
        entity.setAdmin(hunter.isAdmin());

        service.updateHunter(entity);

        return findHunterByNickname(hunter.getUserNickname());
    }

    @Override
    public MushroomHunterDTO updatePassword(MushroomHunterUpdatePasswordDTO hunter) {
        MushroomHunter entity = service.findHunterById(hunter.getId());
        if (!service.updatePassword(entity, hunter.getOldPassword(), hunter.getNewPassword())) {
            //throw new InvalidPasswordException(); TODO
        }

        return findHunterById(hunter.getId());
    }

    @Override
    public List<MushroomHunterDTO> findAllHunters() {
        return beanMappingService.mapTo(service.findAllHunters(), MushroomHunterDTO.class);
    }

    @Override
    public boolean authenticate(MushroomHunterAuthenticateDTO hunter) {
        return service.authenticate(service.findHunterById(hunter.getId()), hunter.getPassword());
    }

    @Override
    public boolean isAdmin(MushroomHunterDTO hunter) {
        return service.isAdmin(beanMappingService.mapTo(hunter, MushroomHunter.class));
    }
}
