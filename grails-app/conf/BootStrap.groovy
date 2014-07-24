import grails.util.Environment

import org.codehaus.groovy.grails.web.json.JSONObject

import com.kott.fr.INode
import com.kott.fr.Role
import com.kott.fr.Tag
import com.kott.fr.User
import com.kott.fr.UserRole

class BootStrap {

	def init = { servletContext ->
		JSONObject.NULL.metaClass.asBoolean = {-> false}

		//initializing data
		Role admin = null
		if(Role.count() < 3){
			new Role(authority: "ROLE_USER").save(failOnError: true)
			new Role(authority: "ROLE_ADMIN").save(failOnError: true)
			new Role(authority: "ROLE_FACEBOOK").save(failOnError: true)
		}

		def userRole = Role.findByAuthority('ROLE_USER_2') ?: new Role(authority: 'ROLE_USER_2').save(failOnError: true)
		def adminRole = Role.findByAuthority('ROLE_ADMIN_2') ?: new Role(authority: 'ROLE_ADMIN_2').save(failOnError: true)

		//add an admin and default user
		if(Environment.current == Environment.DEVELOPMENT){
			def adminUser = User.findByUsername('admin') ?: new User(
					email: "admin@yahoo.fr",
					username: 'admin',
					enabled:true,
					accountExpired:false,
					accountLocked:false,
					passwordExpired:false).save(failOnError: true)

			def basicUser = User.findByUsername('guest') ?: new User(
					email: "user@yahoo.fr",
					username: 'guest',
					enabled:true,
					accountExpired:false,
					accountLocked:false,
					passwordExpired:false).save(failOnError: true)

			if (!adminUser.authorities.contains(adminRole)) {
				UserRole.create adminUser, adminRole
			}
			if (!basicUser.authorities.contains(userRole)) {
				UserRole.create basicUser, userRole
			}

			def test = User.findByUsername('testuser')?: new User( email: "test@yahoo.fr", username: "testuser", password:"testpass",
			enabled:true, accountExpired:false, accountLocked:false, passwordExpired:false ).save(failOnError: true)

			Tag smartFolderTag = Tag.find("from Tag as t where t.value = 'SmartFolder-0.1'")
			Tag destFolderTag = Tag.find("from Tag as t where t.value = 'Destination'")
			if(!smartFolderTag){
				smartFolderTag = new Tag(value: "SmartFolder-0.1").save(flush: true, failOnError: true)
			}
			if(!destFolderTag){
				destFolderTag = new Tag(value: "Destination").save(flush: true, failOnError: true)
			}

			if(!INode.findAll("from INode as i where owner is null and :tag in elements(i.tags)", [tag: smartFolderTag])){
				def directories = ['a', 'b', 'c', 'd']
				for(i in directories){
					INode dir1 = new INode(name: i, filesystemID: 'dummyfsID', mimeType: 'inode/directory', tags: [smartFolderTag]).save(failOnError: true, flush: true)
					for(j in directories){
						INode dir2 = new INode(name: i + j, filesystemID: 'dummyfsID', mimeType: 'inode/directory', tags: [smartFolderTag]).save(failOnError: true, flush: true)
						dir2.parent = dir1
						dir1.addToChildren(dir2)
						dir1.save(failOnError: true, flush: true)
						dir2.save(failOnError: true, flush: true)
						for(k in directories){
							INode dir3 = new INode(name: i + j + k, filesystemID: 'dummyfsID', mimeType: 'inode/directory', tags: [smartFolderTag, destFolderTag]).save(failOnError: true, flush: true)
							dir3.parent = dir2
							dir2.addToChildren(dir3)
							dir2.save(failOnError: true, flush: true)
							dir3.save(failOnError: true, flush: true)
						}
					}
				}
			}

		}
		def destroy = {
		}
	}
}
