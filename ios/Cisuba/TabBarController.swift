//
//  TabBarController.swift
//  Cisuba
//
//  Created by Sung Kyungmo on 2017. 4. 29..
//  Copyright © 2017년 EastBlue. All rights reserved.
//

import UIKit
import ESTabBarController_swift

class TabBarController: ESTabBarController,UITabBarControllerDelegate {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
                self.view.backgroundColor = UIColor.init(white: 245.0 / 255.0, alpha: 1.0)
        self.delegate = self
        self.tabBar.shadowImage = UIImage(named: "transparent")
        self.tabBar.barTintColor = .white
        
        let storyboard_1: UIStoryboard = UIStoryboard(name: "Home", bundle: nil)
        guard let Home = storyboard_1.instantiateInitialViewController()
            else {
                return
        }
        let storyboard_2: UIStoryboard = UIStoryboard(name: "Find", bundle: nil)
        guard let Find = storyboard_2.instantiateInitialViewController()
            else {
                return
        }
        let storyboard_3: UIStoryboard = UIStoryboard(name: "Near", bundle: nil)
        guard let Near = storyboard_3.instantiateInitialViewController()
            else {
                return
        }
        let storyboard_4: UIStoryboard = UIStoryboard(name: "Profile", bundle: nil)
        guard let Profile = storyboard_4.instantiateInitialViewController()
            else {
                return
        }
        let storyboard_5: UIStoryboard = UIStoryboard(name: "More", bundle: nil)
        guard let More = storyboard_5.instantiateInitialViewController()
            else {
                return
        }
        
        
        Home.tabBarItem = ESTabBarItem.init(IrregularityBasicContentView(), title: "홈", image: UIImage(named: "home"), selectedImage: UIImage(named: "home_1"))
        Find.tabBarItem = ESTabBarItem.init(IrregularityBasicContentView(), title: "검색", image: UIImage(named: "find"), selectedImage: UIImage(named: "find_1"))
        Near.tabBarItem = ESTabBarItem.init(IrregularityContentView(), title: "내근처", image: UIImage(named: "home"), selectedImage: UIImage(named: "home_1"))
        Profile.tabBarItem = ESTabBarItem.init(IrregularityBasicContentView(), title: "프로필", image: UIImage(named: "me"), selectedImage: UIImage(named: "me_1"))
        More.tabBarItem = ESTabBarItem.init(IrregularityBasicContentView(), title: "더보기", image: UIImage(named: "favor"), selectedImage: UIImage(named: "favor_1"))
        
        self.viewControllers = [Home, Find, Near, Profile, More]
        
        //        let navigationController = ExampleNavigationController.init(rootViewController: tabBarController)
        //        tabBarController.title = "Example"
        //        return navigationController
        
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}
